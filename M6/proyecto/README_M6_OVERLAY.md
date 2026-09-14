# M6/proyecto - procedencia del ensamblado acumulativo

`M6/proyecto` se publica como evolución física del árbol completo `M5/proyecto`
del commit canónico `0f76aa08385605a48eccbcb3b1be6bf18b9bce1e`.

El paquete de publicación realiza el ensamblado de forma reproducible:

1. clona `main` fresco y verifica el SHA base;
2. copia íntegramente `M5/proyecto` a `M6/proyecto`;
3. superpone únicamente los ficheros nuevos/modificados de M6;
4. ejecuta el gate de continuidad por blob SHA antes de permitir el commit.

Este fichero queda en el repositorio como registro de procedencia. No sustituye
al árbol acumulativo: después del ensamblado, `M6/proyecto` contiene físicamente
todos los ficheros heredados de M5 más la evolución de seguridad/JWT de M6.
