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

EXPECTED_THEORY='bcb8eed02eb65cf4637c1a3333b1c8e96153e9a11e8c0c97d20a93f40f4bcfcd'
EXPECTED_PRACTICE='ff59d72216890ea44481e06840d384efafb288fa2b4d0f1ee184c8a7d8aa14ec'

if [[ "$THEORY_SHA" != "$EXPECTED_THEORY" ]]; then
  echo "ERROR: TEORIA.md no coincide con los bloques LLM validados" >&2
  echo "esperado=$EXPECTED_THEORY actual=$THEORY_SHA" >&2
  exit 1
fi

if [[ "$PRACTICE_SHA" != "$EXPECTED_PRACTICE" ]]; then
  echo "ERROR: PRACTICA.md no coincide con los bloques LLM validados" >&2
  echo "esperado=$EXPECTED_PRACTICE actual=$PRACTICE_SHA" >&2
  exit 1
fi

echo "M0 ensamblado exactamente desde los bloques redactados por el modelo."
echo "TEORIA   $THEORY_SHA"
echo "PRACTICA $PRACTICE_SHA"
