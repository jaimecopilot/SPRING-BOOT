param([switch]$NoPause)

$ErrorActionPreference = 'Stop'
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new($false)
$OutputEncoding = [System.Text.UTF8Encoding]::new($false)

$Root = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $Root
$Stamp = Get-Date -Format 'yyyyMMdd_HHmmss'
$Log = Join-Path $Root "VALIDAR_M6_$Stamp.log"
$BuildId = 'M6-VALIDATOR-FINAL-20260916'
$Failures = New-Object System.Collections.Generic.List[string]

function Write-Log([string]$Text = '') {
    Write-Host $Text
    Add-Content -LiteralPath $Log -Value $Text -Encoding UTF8
}

function Require-Path([string]$Relative) {
    $p = Join-Path $Root $Relative
    if (-not (Test-Path -LiteralPath $p)) {
        Write-Log "[PRECHECK][FAIL] Falta: $Relative"
        $script:Failures.Add("PRECHECK:$Relative")
        return $false
    }
    return $true
}

function Run-Native([string]$Label, [string]$WorkingDir, [string]$Exe, [string[]]$CommandArgs) {
    Write-Log $Label
    Push-Location $WorkingDir
    try {
        Write-Log ("CMD: {0} {1}" -f $Exe, ($CommandArgs -join ' '))
        # Windows PowerShell 5 convierte STDERR de procesos nativos en ErrorRecord.
        # Con ErrorActionPreference=Stop, simples warnings de la JVM (p. ej. CDS)
        # se convierten erroneamente en una excepcion del validador aunque Maven
        # siga ejecutandose correctamente. Durante SOLO el proceso nativo usamos
        # Continue; conservamos el codigo real de salida en $LASTEXITCODE.
        $oldEap = $ErrorActionPreference
        $ErrorActionPreference = 'Continue'
        try {
            & $Exe @CommandArgs 2>&1 | ForEach-Object {
                $line = $_.ToString()
                Write-Host $line
                Add-Content -LiteralPath $Log -Value $line -Encoding UTF8
            }
            $rc = $LASTEXITCODE
        }
        finally {
            $ErrorActionPreference = $oldEap
        }
    } catch {
        $rc = 999
        Write-Log ("EXCEPCION: " + $_.Exception.Message)
    } finally {
        Pop-Location
    }
    if ($rc -eq 0) {
        Write-Log "$Label PASS"
        return $true
    }
    Write-Log "$Label FAIL (codigo $rc)"
    $script:Failures.Add($Label)
    return $false
}

Write-Log '================================================================'
Write-Log 'M6 - VALIDACION ACUMULATIVA 6.1 - 6.9'
Write-Log "VALIDATOR BUILD: $BuildId"
Write-Log "Raiz: $Root"
Write-Log "Log:  $Log"
Write-Log '================================================================'

Write-Log '[PRECHECK] Comprobando paquete completo...'
$required = @('TEORIA.md','PRACTICA.md','VALIDAR_M6_ESTATICO.py')
foreach ($v in @('6.1','6.2','6.3','6.4','6.5','6.6','6.7','6.8','6.9')) {
    $required += "$v\proyecto\pom.xml"
    $required += "$v\proyecto\mvnw.cmd"
}
foreach ($r in $required) { [void](Require-Path $r) }
if ($Failures.Count -gt 0) {
    Write-Log '[PRECHECK] FAIL'
} else {
    Write-Log '[PRECHECK] PASS'

    # Gate estatico: preferir py, luego python.
    $pythonExe = $null
    $pythonArgs = @()
    if (Get-Command py -ErrorAction SilentlyContinue) {
        $pythonExe = 'py'; $pythonArgs = @('-3','VALIDAR_M6_ESTATICO.py')
    } elseif (Get-Command python -ErrorAction SilentlyContinue) {
        $pythonExe = 'python'; $pythonArgs = @('VALIDAR_M6_ESTATICO.py')
    }
    if ($pythonExe) {
        [void](Run-Native '[0/11] Gate estatico' $Root $pythonExe $pythonArgs)
    } else {
        Write-Log '[0/11] Gate estatico FAIL: no se encuentra Python/py'
        $Failures.Add('[0/11] Gate estatico')
    }

    $projectPass = @{}
    foreach ($v in @('6.1','6.2','6.3','6.4','6.5','6.6','6.7','6.8','6.9')) {
        $wd = Join-Path $Root "$v\proyecto"
        $ok = Run-Native "[$v] clean test" $wd '.\mvnw.cmd' @('-B','clean','test')
        $projectPass[$v] = $ok
    }

    if ($projectPass['6.5']) {
        $wd = Join-Path $Root '6.5\proyecto'
        [void](Run-Native '[10/11] Observable manual JWT 6.5' $wd 'java' @('-cp','target\test-classes','es.mecd.demo.miproyecto.jwt.JwtManual'))
    } else {
        Write-Log '[10/11] SKIP Observable manual JWT 6.5: 6.5 clean test fallo'
        $Failures.Add('[10/11] Observable manual JWT 6.5 (SKIP)')
    }

    if ($projectPass['6.9']) {
        $wd = Join-Path $Root '6.9\proyecto'
        [void](Run-Native '[11/11] Gate final 6.9' $wd '.\mvnw.cmd' @('-B','-Dtest=AuthControllerSecurityTest,UsuarioControllerCompleteSecurityTest,GestorSecurityTest,PublicControllerSecurityTest','test'))
    } else {
        Write-Log '[11/11] SKIP Gate final 6.9: 6.9 clean test fallo'
        $Failures.Add('[11/11] Gate final 6.9 (SKIP)')
    }
}

Write-Log '================================================================'
if ($Failures.Count -eq 0) {
    Write-Log 'M6 VALIDACION: PASS'
    $exitCode = 0
} else {
    Write-Log 'M6 VALIDACION: FAIL'
    Write-Log ('Fallos: ' + (($Failures | Select-Object -Unique) -join ' | '))
    $exitCode = 1
}
Write-Log "Log completo: $Log"
Write-Log '================================================================'

if (-not $NoPause) {
    Write-Host ''
    Write-Host 'La ventana NO se cerrara automaticamente.'
    Read-Host 'Pulsa ENTER cuando hayas copiado o localizado el log' | Out-Null
}
exit $exitCode
