param(
    [switch]$NoPause
)

$ErrorActionPreference = 'Stop'
Set-StrictMode -Version 2.0
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new($false)
$OutputEncoding = [System.Text.UTF8Encoding]::new($false)
Add-Type -AssemblyName System.Net.Http

$Root = Split-Path -Parent $MyInvocation.MyCommand.Path
$Stamp = Get-Date -Format 'yyyyMMdd_HHmmss'
$MainLog = Join-Path $Root ("VALIDAR_M6_HTTP_{0}.log" -f $Stamp)
$AppsLogDir = Join-Path $Root ("VALIDAR_M6_HTTP_APPS_{0}" -f $Stamp)
New-Item -ItemType Directory -Path $AppsLogDir -Force | Out-Null

$script:Http = New-Object System.Net.Http.HttpClient
$script:Http.Timeout = [TimeSpan]::FromSeconds(15)
$script:Checks = 0
$script:CurrentProcess = $null
$script:Failures = New-Object System.Collections.Generic.List[string]

function Write-Log([string]$Text) {
    Write-Host $Text
    [System.IO.File]::AppendAllText($MainLog, $Text + [Environment]::NewLine, [Text.Encoding]::UTF8)
}

function Fail([string]$Message) {
    throw $Message
}

function New-Request(
    [string]$Method,
    [string]$Url,
    [string]$Body,
    [hashtable]$Headers,
    [string]$BasicUser,
    [string]$BasicPassword
) {
    $httpMethod = New-Object System.Net.Http.HttpMethod -ArgumentList $Method
    $request = New-Object System.Net.Http.HttpRequestMessage -ArgumentList $httpMethod, $Url

    if (-not [string]::IsNullOrEmpty($Body)) {
        $request.Content = [System.Net.Http.StringContent]::new($Body, [Text.Encoding]::UTF8, 'application/json')
    }

    if ($BasicUser) {
        $raw = '{0}:{1}' -f $BasicUser, $BasicPassword
        $encoded = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes($raw))
        [void]$request.Headers.TryAddWithoutValidation('Authorization', 'Basic ' + $encoded)
    }

    if ($Headers) {
        foreach ($key in $Headers.Keys) {
            [void]$request.Headers.TryAddWithoutValidation([string]$key, [string]$Headers[$key])
        }
    }
    return $request
}

function Invoke-Check {
    param(
        [string]$Label,
        [string]$Method,
        [string]$Url,
        [int]$Expected,
        [AllowNull()][string]$Body = $null,
        [AllowNull()][hashtable]$Headers = $null,
        [AllowNull()][string]$BasicUser = $null,
        [AllowNull()][string]$BasicPassword = $null,
        [AllowNull()][string]$Contains = $null
    )

    $request = New-Request $Method $Url $Body $Headers $BasicUser $BasicPassword
    $response = $null
    try {
        $response = $script:Http.SendAsync($request).GetAwaiter().GetResult()
        $text = $response.Content.ReadAsStringAsync().GetAwaiter().GetResult()
        $status = [int]$response.StatusCode
    }
    finally {
        if ($null -ne $response) { $response.Dispose() }
        $request.Dispose()
    }

    $script:Checks++
    if ($status -ne $Expected) {
        Write-Log ("[FAIL] {0}: esperado {1}, recibido {2}" -f $Label, $Expected, $status)
        if ($text) { Write-Log ('       Body: ' + $text) }
        Fail ("Fallo HTTP en: {0}" -f $Label)
    }
    if ($Contains -and ($text -notlike ('*' + $Contains + '*'))) {
        Write-Log ("[FAIL] {0}: status correcto, pero el body no contiene: {1}" -f $Label, $Contains)
        if ($text) { Write-Log ('       Body: ' + $text) }
        Fail ("Contenido HTTP inesperado en: {0}" -f $Label)
    }

    Write-Log ("[PASS] {0} -> HTTP {1}" -f $Label, $status)
    return [pscustomobject]@{
        Status = $status
        Body = $text
    }
}

function Probe([string]$Url) {
    $request = New-Request 'GET' $Url $null $null $null $null
    try {
        $response = $script:Http.SendAsync($request).GetAwaiter().GetResult()
        [void]$response.Content.ReadAsStringAsync().GetAwaiter().GetResult()
        $response.Dispose()
        return $true
    }
    catch {
        return $false
    }
    finally {
        $request.Dispose()
    }
}

function Probe-Status(
    [string]$Method,
    [string]$Url,
    [AllowNull()][string]$Body = $null,
    [AllowNull()][string]$BasicUser = $null,
    [AllowNull()][string]$BasicPassword = $null
) {
    $request = New-Request $Method $Url $Body $null $BasicUser $BasicPassword
    $response = $null
    try {
        $response = $script:Http.SendAsync($request).GetAwaiter().GetResult()
        [void]$response.Content.ReadAsStringAsync().GetAwaiter().GetResult()
        return [int]$response.StatusCode
    }
    catch {
        return -1
    }
    finally {
        if ($null -ne $response) { $response.Dispose() }
        $request.Dispose()
    }
}

function Wait-InitialData([string]$Version, [string]$Base) {
    # El servidor embebido puede empezar a aceptar HTTP antes de que terminen
    # los CommandLineRunner. /public/info solo demuestra que Tomcat escucha,
    # no que los usuarios iniciales ya hayan sido insertados en H2.
    $method = $null
    $url = $null
    $body = $null
    $basicUser = $null
    $basicPassword = $null

    switch ($Version) {
        '6.3' { $method='GET';  $url=$Base + '/api/v1/perfil'; $basicUser='ana'; $basicPassword='ana123' }
        '6.4' { $method='GET';  $url=$Base + '/api/v1/perfil'; $basicUser='ana'; $basicPassword='ana123' }
        '6.5' { $method='GET';  $url=$Base + '/api/v1/perfil'; $basicUser='ana'; $basicPassword='ana123' }
        '6.6' { $method='GET';  $url=$Base + '/api/v1/perfil'; $basicUser='ana'; $basicPassword='ana123' }
        '6.7' { $method='POST'; $url=$Base + '/api/v1/auth/login'; $body='{"username":"ana","password":"ana123"}' }
        '6.8' { $method='POST'; $url=$Base + '/api/v1/auth/login'; $body='{"username":"gestor","password":"gestor123"}' }
        '6.9' { $method='POST'; $url=$Base + '/api/v1/auth/login'; $body='{"username":"gestor","password":"gestor123"}' }
        default { return }
    }

    for ($i = 0; $i -lt 40; $i++) {
        if ($script:CurrentProcess.HasExited) {
            Fail ("La aplicacion {0} termino mientras se esperaba la carga de datos iniciales" -f $Version)
        }
        $status = Probe-Status $method $url $body $basicUser $basicPassword
        if ($status -eq 200) {
            Write-Log ("[{0}] Datos iniciales confirmados" -f $Version)
            return
        }
        Start-Sleep -Milliseconds 250
    }
    Fail ("{0}: el servidor responde, pero los datos iniciales no estuvieron disponibles en 10 s" -f $Version)
}

function Stop-CurrentApp {
    if ($null -ne $script:CurrentProcess) {
        try {
            if (-not $script:CurrentProcess.HasExited) {
                Stop-Process -Id $script:CurrentProcess.Id -Force -ErrorAction SilentlyContinue
                Start-Sleep -Milliseconds 500
            }
        } catch { }
        $script:CurrentProcess = $null
    }
}

function Start-App([string]$Version, [int]$Port) {
    Stop-CurrentApp
    $project = Join-Path $Root (Join-Path $Version 'proyecto')
    $mvnw = Join-Path $project 'mvnw.cmd'
    if (-not (Test-Path $mvnw)) { Fail ("No existe {0}" -f $mvnw) }

    Write-Log ''
    Write-Log ("[{0}] Empaquetando aplicacion para prueba HTTP real..." -f $Version)
    $buildLog = Join-Path $AppsLogDir ("{0}_package.log" -f $Version)
    Push-Location $project
    try {
        & .\mvnw.cmd -q -DskipTests package *> $buildLog
        $exit = $LASTEXITCODE
    }
    finally {
        Pop-Location
    }
    if ($exit -ne 0) {
        Write-Log ("[FAIL] {0}: mvnw package termino con codigo {1}. Log: {2}" -f $Version, $exit, $buildLog)
        Fail ("No se pudo empaquetar {0}" -f $Version)
    }

    $jar = Get-ChildItem (Join-Path $project 'target') -Filter '*.jar' |
        Where-Object { $_.Name -notlike '*.original' } |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1
    if ($null -eq $jar) { Fail ("No se encontro JAR ejecutable en {0}" -f $Version) }

    $stdout = Join-Path $AppsLogDir ("{0}_app.out.log" -f $Version)
    $stderr = Join-Path $AppsLogDir ("{0}_app.err.log" -f $Version)
    $java = (Get-Command java.exe -ErrorAction Stop).Source
    $args = @('-jar', ('"' + $jar.FullName + '"'), ("--server.port={0}" -f $Port), '--spring.profiles.active=dev')
    $script:CurrentProcess = Start-Process -FilePath $java -ArgumentList $args -WorkingDirectory $project `
        -RedirectStandardOutput $stdout -RedirectStandardError $stderr -PassThru -WindowStyle Hidden

    $readyUrl = "http://localhost:{0}/api/v1/public/info" -f $Port
    $ready = $false
    for ($i = 0; $i -lt 75; $i++) {
        if ($script:CurrentProcess.HasExited) {
            Write-Log ("[FAIL] {0}: la aplicacion termino durante el arranque. Logs: {1} / {2}" -f $Version, $stdout, $stderr)
            Fail ("La aplicacion {0} no arranco" -f $Version)
        }
        if (Probe $readyUrl) { $ready = $true; break }
        Start-Sleep -Seconds 1
    }
    if (-not $ready) {
        Write-Log ("[FAIL] {0}: no respondio en 75 s. Logs: {1} / {2}" -f $Version, $stdout, $stderr)
        Fail ("Timeout arrancando {0}" -f $Version)
    }
    $base = ("http://localhost:{0}" -f $Port)
    Wait-InitialData $Version $base
    Write-Log ("[{0}] Aplicacion REAL arrancada y preparada en puerto {1}" -f $Version, $Port)
    return $base
}

function Login-Jwt([string]$Base, [string]$User, [string]$Password) {
    $json = @{ username = $User; password = $Password } | ConvertTo-Json -Compress
    $r = Invoke-Check ("login JWT {0}" -f $User) 'POST' ($Base + '/api/v1/auth/login') 200 -Body $json
    $o = $r.Body | ConvertFrom-Json
    if (-not $o.access_token -or -not $o.refresh_token -or $o.token_type -ne 'Bearer') {
        Fail ("Login de {0} no devolvio access_token/refresh_token/Bearer" -f $User)
    }
    return $o
}

function Bearer([string]$Token) {
    return @{ Authorization = 'Bearer ' + $Token }
}

function Run-61 {
    $b = Start-App '6.1' 18061
    Invoke-Check '6.1 publico sin credenciales' 'GET' ($b + '/api/v1/public/info') 200 | Out-Null
    Invoke-Check '6.1 recurso protegido sin autenticar' 'GET' ($b + '/api/v1/alumnos') 401 | Out-Null
    Stop-CurrentApp
}

function Run-62 {
    $b = Start-App '6.2' 18062
    Invoke-Check '6.2 publico sin credenciales' 'GET' ($b + '/api/v1/public/info') 200 | Out-Null
    Invoke-Check '6.2 alumnos anonimo' 'GET' ($b + '/api/v1/alumnos') 401 | Out-Null
    Invoke-Check '6.2 ana accede a perfil' 'GET' ($b + '/api/v1/perfil') 200 -BasicUser 'ana' -BasicPassword 'ana123' -Contains 'ana' | Out-Null
    Invoke-Check '6.2 credenciales incorrectas' 'GET' ($b + '/api/v1/perfil') 401 -BasicUser 'ana' -BasicPassword 'incorrecta' | Out-Null
    Invoke-Check '6.2 USER no accede a admin' 'GET' ($b + '/api/v1/admin/usuarios') 403 -BasicUser 'ana' -BasicPassword 'ana123' | Out-Null
    Invoke-Check '6.2 USER no accede a gestor' 'GET' ($b + '/api/v1/gestor/documentos') 403 -BasicUser 'ana' -BasicPassword 'ana123' | Out-Null
    Invoke-Check '6.2 GESTOR accede a gestor' 'GET' ($b + '/api/v1/gestor/documentos') 200 -BasicUser 'gestor' -BasicPassword 'gestor123' | Out-Null
    Invoke-Check '6.2 ADMIN accede a gestor' 'GET' ($b + '/api/v1/gestor/documentos') 200 -BasicUser 'admin' -BasicPassword 'admin123' | Out-Null
    Stop-CurrentApp
}

function Run-63 {
    $b = Start-App '6.3' 18063
    Invoke-Check '6.3 publico sin credenciales' 'GET' ($b + '/api/v1/public/info') 200 | Out-Null
    Invoke-Check '6.3 ana persistida autentica' 'GET' ($b + '/api/v1/perfil') 200 -BasicUser 'ana' -BasicPassword 'ana123' -Contains 'ROLE_USER' | Out-Null
    Invoke-Check '6.3 gestor de 6.2 ya no existe' 'GET' ($b + '/api/v1/perfil') 401 -BasicUser 'gestor' -BasicPassword 'gestor123' | Out-Null
    $reg = '{"username":"pedro_http","password":"pedro1234","email":"pedro_http@example.com"}'
    $rr = Invoke-Check '6.3 registro publico' 'POST' ($b + '/api/v1/auth/registro') 201 -Body $reg
    if ($rr.Body -like '*password*') { Fail '6.3: la respuesta de registro expone password' }
    Invoke-Check '6.3 usuario registrado autentica' 'GET' ($b + '/api/v1/perfil') 200 -BasicUser 'pedro_http' -BasicPassword 'pedro1234' -Contains 'pedro_http' | Out-Null
    $chg = '{"passwordActual":"pedro1234","passwordNueva":"pedro5678"}'
    Invoke-Check '6.3 cambio de contrasena autenticado' 'PUT' ($b + '/api/v1/perfil/password') 204 -Body $chg -BasicUser 'pedro_http' -BasicPassword 'pedro1234' | Out-Null
    Invoke-Check '6.3 contrasena antigua deja de servir' 'GET' ($b + '/api/v1/perfil') 401 -BasicUser 'pedro_http' -BasicPassword 'pedro1234' | Out-Null
    Invoke-Check '6.3 contrasena nueva funciona' 'GET' ($b + '/api/v1/perfil') 200 -BasicUser 'pedro_http' -BasicPassword 'pedro5678' | Out-Null
    Stop-CurrentApp
}

function Run-64Like([string]$Version, [int]$Port) {
    $b = Start-App $Version $Port
    Invoke-Check ("{0} admin anonimo" -f $Version) 'GET' ($b + '/api/v1/admin/usuarios') 401 | Out-Null
    Invoke-Check ("{0} USER recibe 403 en admin" -f $Version) 'GET' ($b + '/api/v1/admin/usuarios') 403 -BasicUser 'ana' -BasicPassword 'ana123' | Out-Null
    $list = Invoke-Check ("{0} ADMIN lista usuarios" -f $Version) 'GET' ($b + '/api/v1/admin/usuarios') 200 -BasicUser 'admin' -BasicPassword 'admin123'
    Invoke-Check ("{0} perfil propio" -f $Version) 'GET' ($b + '/api/v1/perfil') 200 -BasicUser 'ana' -BasicPassword 'ana123' -Contains 'ana' | Out-Null
    $users = $list.Body | ConvertFrom-Json
    $ana = @($users | Where-Object { $_.username -eq 'ana' }) | Select-Object -First 1
    if ($null -eq $ana -or -not $ana.identificador) { Fail ("{0}: no se pudo localizar el id de ana" -f $Version) }
    $roles = '{"roles":["USER"]}'
    Invoke-Check ("{0} ADMIN puede cambiar roles" -f $Version) 'PUT' ($b + '/api/v1/admin/usuarios/' + $ana.identificador + '/roles') 200 -Body $roles -BasicUser 'admin' -BasicPassword 'admin123' | Out-Null
    Invoke-Check ("{0} USER no puede cambiar roles" -f $Version) 'PUT' ($b + '/api/v1/admin/usuarios/' + $ana.identificador + '/roles') 403 -Body $roles -BasicUser 'ana' -BasicPassword 'ana123' | Out-Null
    Stop-CurrentApp
}

function Run-66 {
    $b = Start-App '6.6' 18066
    $login = Login-Jwt $b 'ana' 'ana123'
    Invoke-Check '6.6 login incorrecto -> 401' 'POST' ($b + '/api/v1/auth/login') 401 -Body '{"username":"ana","password":"incorrecta"}' | Out-Null
    Invoke-Check '6.6 paso 11: refresh invalido -> 400' 'POST' ($b + '/api/v1/auth/refresh') 400 -Body '{"refresh_token":"tokenInvalido"}' | Out-Null
    Invoke-Check '6.6 Bearer aun NO autentica antes del filtro 6.7' 'GET' ($b + '/api/v1/perfil') 401 -Headers (Bearer $login.access_token) | Out-Null
    $refreshBody = @{ refresh_token = $login.refresh_token } | ConvertTo-Json -Compress
    $fresh = Invoke-Check '6.6 refresh valido emite nuevo par' 'POST' ($b + '/api/v1/auth/refresh') 200 -Body $refreshBody
    $freshJson = $fresh.Body | ConvertFrom-Json
    if (-not $freshJson.access_token -or -not $freshJson.refresh_token) { Fail '6.6: refresh no devolvio nuevo par' }
    Invoke-Check '6.6 refresh antiguo/invalido -> 400' 'POST' ($b + '/api/v1/auth/refresh') 400 -Body $refreshBody | Out-Null
    Invoke-Check '6.6 logout acepta access token' 'POST' ($b + '/api/v1/auth/logout') 204 -Headers (Bearer $login.access_token) | Out-Null
    Stop-CurrentApp
}

function Run-67 {
    $b = Start-App '6.7' 18067
    $ana = Login-Jwt $b 'ana' 'ana123'
    Invoke-Check '6.7 paso 5: perfil sin token -> 401' 'GET' ($b + '/api/v1/perfil') 401 | Out-Null
    Invoke-Check '6.7 paso 5: perfil con token -> 200' 'GET' ($b + '/api/v1/perfil') 200 -Headers (Bearer $ana.access_token) -Contains 'ana' | Out-Null
    Invoke-Check '6.7 paso 5: publico sin token -> 200' 'GET' ($b + '/api/v1/public/info') 200 | Out-Null
    Invoke-Check '6.7 paso 6: UsuarioPrincipal contiene ROLE_USER' 'GET' ($b + '/api/v1/perfil') 200 -Headers (Bearer $ana.access_token) -Contains 'ROLE_USER' | Out-Null

    $admin = Login-Jwt $b 'admin' 'admin123'
    Invoke-Check '6.7 paso 7: ADMIN accede a admin' 'GET' ($b + '/api/v1/admin/usuarios') 200 -Headers (Bearer $admin.access_token) | Out-Null
    Invoke-Check '6.7 paso 7: USER recibe 403 en admin' 'GET' ($b + '/api/v1/admin/usuarios') 403 -Headers (Bearer $ana.access_token) | Out-Null

    Invoke-Check '6.7 paso 8: token invalido -> 401' 'GET' ($b + '/api/v1/perfil') 401 -Headers @{Authorization='Bearer tokenInvalido'} | Out-Null
    Invoke-Check '6.7 paso 9: cabecera sin prefijo Bearer -> 401' 'GET' ($b + '/api/v1/perfil') 401 -Headers @{Authorization=$ana.access_token} | Out-Null

    # Reto 6.7 del PDF. El inicializador crea admin primero (id=1) y ana despues (id=2).
    Invoke-Check '6.7 paso 12: Ana consulta su id=2 -> 200' 'GET' ($b + '/api/v1/perfil/2') 200 -Headers (Bearer $ana.access_token) -Contains 'ana' | Out-Null
    Invoke-Check '6.7 paso 12: Ana consulta id=1 -> 403' 'GET' ($b + '/api/v1/perfil/1') 403 -Headers (Bearer $ana.access_token) | Out-Null
    Invoke-Check '6.7 paso 12: Admin consulta id=2 -> 200' 'GET' ($b + '/api/v1/perfil/2') 200 -Headers (Bearer $admin.access_token) -Contains 'admin' | Out-Null
    Stop-CurrentApp
}
function Run-68 {
    $b = Start-App '6.8' 18068
    Invoke-Check '6.8 sin token -> 401 JSON' 'GET' ($b + '/api/v1/perfil') 401 -Contains 'NO_AUTENTICADO' | Out-Null
    Invoke-Check '6.8 publico sin token -> 200' 'GET' ($b + '/api/v1/public/info') 200 | Out-Null

    $gestor = Login-Jwt $b 'gestor' 'gestor123'
    Invoke-Check '6.8 GESTOR perfil -> 200' 'GET' ($b + '/api/v1/perfil') 200 -Headers (Bearer $gestor.access_token) | Out-Null
    Invoke-Check '6.8 paso 12: perfil/usuario desde claims -> 200' 'GET' ($b + '/api/v1/perfil/usuario') 200 -Headers (Bearer $gestor.access_token) -Contains 'gestor' | Out-Null
    Invoke-Check '6.8 GESTOR zona gestor -> 200' 'GET' ($b + '/api/v1/gestor/documentos') 200 -Headers (Bearer $gestor.access_token) | Out-Null
    Invoke-Check '6.8 GESTOR zona admin -> 403 JSON' 'GET' ($b + '/api/v1/admin/usuarios') 403 -Headers (Bearer $gestor.access_token) -Contains 'ACCESO_DENEGADO' | Out-Null

    $ana = Login-Jwt $b 'ana' 'ana123'
    Invoke-Check '6.8 USER zona gestor -> 403 JSON' 'GET' ($b + '/api/v1/gestor/documentos') 403 -Headers (Bearer $ana.access_token) -Contains 'ACCESO_DENEGADO' | Out-Null
    Invoke-Check '6.8 conserva reto 6.7: Ana id=1 -> 403 JSON' 'GET' ($b + '/api/v1/perfil/1') 403 -Headers (Bearer $ana.access_token) -Contains 'ACCESO_DENEGADO' | Out-Null

    $admin = Login-Jwt $b 'admin' 'admin123'
    Invoke-Check '6.8 ADMIN zona admin -> 200' 'GET' ($b + '/api/v1/admin/usuarios') 200 -Headers (Bearer $admin.access_token) | Out-Null
    Invoke-Check '6.8 ADMIN zona gestor -> 200' 'GET' ($b + '/api/v1/gestor/documentos') 200 -Headers (Bearer $admin.access_token) | Out-Null
    Invoke-Check '6.8 conserva reto 6.7: Admin id=2 -> 200' 'GET' ($b + '/api/v1/perfil/2') 200 -Headers (Bearer $admin.access_token) -Contains 'admin' | Out-Null

    $refreshBody = @{ refresh_token = $ana.refresh_token } | ConvertTo-Json -Compress
    Invoke-Check '6.8 refresh primero -> 200' 'POST' ($b + '/api/v1/auth/refresh') 200 -Body $refreshBody | Out-Null
    Invoke-Check '6.8 refresh reutilizado -> 400' 'POST' ($b + '/api/v1/auth/refresh') 400 -Body $refreshBody | Out-Null

    $logoutPair = Login-Jwt $b 'ana' 'ana123'
    Invoke-Check '6.8 antes de logout perfil -> 200' 'GET' ($b + '/api/v1/perfil') 200 -Headers (Bearer $logoutPair.access_token) | Out-Null
    Invoke-Check '6.8 logout -> 204' 'POST' ($b + '/api/v1/auth/logout') 204 -Headers (Bearer $logoutPair.access_token) | Out-Null
    Invoke-Check '6.8 token revocado -> 401' 'GET' ($b + '/api/v1/perfil') 401 -Headers (Bearer $logoutPair.access_token) -Contains 'NO_AUTENTICADO' | Out-Null
    Stop-CurrentApp
}

function Run-69 {
    $b = Start-App '6.9' 18069
    Invoke-Check '6.9 publico -> 200' 'GET' ($b + '/api/v1/public/info') 200 | Out-Null
    Invoke-Check '6.9 anonimo admin -> 401 JSON' 'GET' ($b + '/api/v1/admin/usuarios') 401 -Contains 'NO_AUTENTICADO' | Out-Null
    $ana = Login-Jwt $b 'ana' 'ana123'
    Invoke-Check '6.9 JWT valido -> perfil 200' 'GET' ($b + '/api/v1/perfil') 200 -Headers (Bearer $ana.access_token) -Contains 'ana' | Out-Null
    Invoke-Check '6.9 USER admin -> 403 JSON' 'GET' ($b + '/api/v1/admin/usuarios') 403 -Headers (Bearer $ana.access_token) -Contains 'ACCESO_DENEGADO' | Out-Null
    Invoke-Check '6.9 JWT invalido -> 401' 'GET' ($b + '/api/v1/perfil') 401 -Headers @{Authorization='Bearer tokenInvalido'} -Contains 'NO_AUTENTICADO' | Out-Null
    $admin = Login-Jwt $b 'admin' 'admin123'
    Invoke-Check '6.9 ADMIN admin -> 200' 'GET' ($b + '/api/v1/admin/usuarios') 200 -Headers (Bearer $admin.access_token) | Out-Null

    $gestor = Login-Jwt $b 'gestor' 'gestor123'
    Invoke-Check '6.9 conserva 6.8: GESTOR perfil -> 200' 'GET' ($b + '/api/v1/perfil') 200 -Headers (Bearer $gestor.access_token) -Contains 'gestor' | Out-Null
    Invoke-Check '6.9 conserva 6.8: GESTOR zona gestor -> 200' 'GET' ($b + '/api/v1/gestor/documentos') 200 -Headers (Bearer $gestor.access_token) | Out-Null
    Invoke-Check '6.9 conserva 6.8: GESTOR zona admin -> 403' 'GET' ($b + '/api/v1/admin/usuarios') 403 -Headers (Bearer $gestor.access_token) -Contains 'ACCESO_DENEGADO' | Out-Null

    $logout = Login-Jwt $b 'ana' 'ana123'
    Invoke-Check '6.9 logout real -> 204' 'POST' ($b + '/api/v1/auth/logout') 204 -Headers (Bearer $logout.access_token) | Out-Null
    Invoke-Check '6.9 token revocado -> 401' 'GET' ($b + '/api/v1/perfil') 401 -Headers (Bearer $logout.access_token) | Out-Null
    Stop-CurrentApp
}


function Run-Safely([string]$Label, [scriptblock]$Action) {
    try {
        & $Action
    }
    catch {
        Stop-CurrentApp
        $msg = ("{0}: {1}" -f $Label, $_.Exception.Message)
        $script:Failures.Add($msg)
        Write-Log ("[VERSION FAIL] " + $msg)
    }
}

try {
    Write-Log '================================================================'
    Write-Log 'M6 - GATE HTTP REAL / EXTREMO A EXTREMO'
    Write-Log 'HTTP VALIDATOR BUILD: M6-HTTP-FINAL-20260916'
    Write-Log ("Raiz: {0}" -f $Root)
    Write-Log ("Log:  {0}" -f $MainLog)
    Write-Log '================================================================'

    if (-not (Get-Command java.exe -ErrorAction SilentlyContinue)) { Fail 'java.exe no esta en PATH' }
    foreach ($v in @('6.1','6.2','6.3','6.4','6.5','6.6','6.7','6.8','6.9')) {
        if (-not (Test-Path (Join-Path $Root (Join-Path $v 'proyecto\mvnw.cmd')))) {
            Fail ("Falta {0}\proyecto\mvnw.cmd. Ejecuta el BAT dentro del paquete completo." -f $v)
        }
    }
    Write-Log '[PRECHECK] PASS'

    Run-Safely '6.1' { Run-61 }
    Run-Safely '6.2' { Run-62 }
    Run-Safely '6.3' { Run-63 }
    Run-Safely '6.4' { Run-64Like '6.4' 18064 }
    # 6.5 conserva el servidor de 6.4 y anade el laboratorio JWT manual, cubierto ademas por VALIDAR_M6.bat.
    Run-Safely '6.5' { Run-64Like '6.5' 18065 }
    Run-Safely '6.6' { Run-66 }
    Run-Safely '6.7' { Run-67 }
    Run-Safely '6.8' { Run-68 }
    Run-Safely '6.9' { Run-69 }

    Write-Log ''
    Write-Log '================================================================'
    if ($script:Failures.Count -eq 0) {
        Write-Log ("M6 HTTP REAL: PASS | comprobaciones HTTP={0}" -f $script:Checks)
        Write-Log 'Se han arrancado realmente los snapshots 6.1-6.9 y se han realizado peticiones HTTP.'
        $exitCode = 0
    } else {
        Write-Log ("M6 HTTP REAL: FAIL | fallos={0} | comprobaciones HTTP={1}" -f $script:Failures.Count, $script:Checks)
        foreach ($failure in $script:Failures) { Write-Log (" - " + $failure) }
        $exitCode = 1
    }
    Write-Log ("Logs de aplicaciones: {0}" -f $AppsLogDir)
    Write-Log '================================================================'
}
catch {
    Write-Log ''
    Write-Log '================================================================'
    Write-Log 'M6 HTTP REAL: FAIL'
    Write-Log ('Error: ' + $_.Exception.Message)
    Write-Log ("Log completo: {0}" -f $MainLog)
    Write-Log ("Logs de aplicaciones: {0}" -f $AppsLogDir)
    Write-Log '================================================================'
    $exitCode = 1
}
finally {
    Stop-CurrentApp
    if ($null -ne $script:Http) { $script:Http.Dispose() }
}

if (-not $NoPause) {
    Write-Host ''
    Write-Host 'La ventana NO se cerrara automaticamente.'
    Write-Host 'Pulsa una tecla cuando hayas copiado o localizado el log.'
    [void][Console]::ReadKey($true)
}
exit $exitCode
