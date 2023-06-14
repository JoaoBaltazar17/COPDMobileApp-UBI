# Sprint 13 - Notes 

# Order of The Week

- [x]  1. Reunir com pessoal da FCS (Marcar e descrever temas pertinentes).

- [x]  2. Continuação do Relatório.

- [ ]  3. Adicionar o Wellness Value 

## 1. Reunião Estado: 13/06

## 3. WellNess Value

- Score (Conceitual): a intenção "final" seria também integrar com info clínica e de medicação do doente    para criar um score com mais valor clínico.
  - Percentagens:
    - Sintomas respiratórios, 10
    - usos de corticosteroides, 10
    - uso dos outros medicamentos: inalador 5; nebulizador 5; medicação oral 5; antibióticos 5
    - hospitalização/intubação/uso de oxigénio 30
    - CAT 15
    - Testes físicos 15

- Score (Pratico): 
  - Percentagens:
    - Variaveis_Sensor 40%
    - TestesFisicos 30%
    - Questionario 30%


### 3.1 Questionário - Pontuação

- Metodologia: Analisar o último questionário feito pelo doente e quantificá-lo

Exemplo:                        CAT1 - 5 [0-16]                          |||| CAT2 - 12 [0-16]

Procedimento de Normalização:   CAT1 - (100 * 5) / 16 =  31.25% [0-100]  |||| CAT2 - (100 * 12) / 16 =  75[0-100]

Input na fórmula: CAT1 - 68.75% (100 - 31.25) (Escala do pior 0% para o melhor 100%) 
                  CAT2 - 25% (100 - 75) (Escala do pior 0% para o melhor 100%) 

### 3.2 Testes Fisicos - Pontuação

- Metodologia: Analisar o último questionário feito pelo doente e quantificá-lo

