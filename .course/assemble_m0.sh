#!/usr/bin/env bash
set -euo pipefail

mkdir -p M0

cat \
  .course/staging/M0/theory_1_01.md \
  .course/staging/M0/theory_1_02.md \
  .course/staging/M0/theory_1_03.md \
  .course/staging/M0/theory_2_01.md \
  .course/staging/M0/theory_2_02.md \
  .course/staging/M0/theory_3.md \
  .course/staging/M0/theory_4_01.md \
  .course/staging/M0/theory_4_02.md \
  .course/staging/M0/theory_4_03.md \
  > M0/TEORIA.md

cat \
  .course/staging/M0/practice_1_01.md \
  .course/staging/M0/practice_1_02.md \
  .course/staging/M0/practice_2_01.md \
  .course/staging/M0/practice_3_01.md \
  .course/staging/M0/practice_3_02.md \
  .course/staging/M0/practice_4_01.md \
  .course/staging/M0/practice_4_02.md \
  .course/staging/M0/practice_4_03.md \
  .course/staging/M0/practice_4_04.md \
  > M0/PRACTICA.md

THEORY_SHA=$(sha256sum M0/TEORIA.md | awk '{print $1}')
PRACTICE_SHA=$(sha256sum M0/PRACTICA.md | awk '{print $1}')

EXPECTED_THEORY='c86511007e6ec9c7c20b149607089e2dae15f38a0f73cc48ec2bf7a0fd535b26'
EXPECTED_PRACTICE='3395162019faaf960a0da29509c55a6c3720826c9306a5b40650603373a58074'

if [[ "$THEORY_SHA" != "$EXPECTED_THEORY" ]]; then
  echo "ERROR: TEORIA.md no coincide con el original LLM validado" >&2
  echo "esperado=$EXPECTED_THEORY actual=$THEORY_SHA" >&2
  exit 1
fi

if [[ "$PRACTICE_SHA" != "$EXPECTED_PRACTICE" ]]; then
  echo "ERROR: PRACTICA.md no coincide con el original LLM validado" >&2
  echo "esperado=$EXPECTED_PRACTICE actual=$PRACTICE_SHA" >&2
  exit 1
fi

echo "M0 ensamblado exactamente desde los bloques redactados por el modelo."
echo "TEORIA   $THEORY_SHA"
echo "PRACTICA $PRACTICE_SHA"
