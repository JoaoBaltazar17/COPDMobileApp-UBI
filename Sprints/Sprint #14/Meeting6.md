# Meeting 6 (12/06/2023)

## 1. Wellness Value
  - Escala (0 - ?)
    - Valores Intermediários:
      - Categorias: 
        - **Sem dados suficientes para avaliação**
          - Feedback de Cores: Branco / Cinzento
          - Dar esta informação quando o doente abre a app pela 1ºvez.
          - Disponibilizar informação de como ter valores suficientes para avaliação, através de um botão de ajuda ao lado do wellness.
        - **[0, 30]**
          - Feedback de Cores: Vermelho
        - **[30, 65]**
          - Feedback de Cores: Amarelo
        - **[65, 100]**
          - Feedback de Cores: Verde
  
  - Fórmula (Praticial): 
    - **Variáveis Sensor** 
      - Análise de valores recolhidos nas últimas (24h (?)).
        - Tratamento destes resultados (detetar valores anormais, quais são os valores anormais (?) )
        - Peso de cada variável (Média Aritmética ou Ponderada (?) )
          - Medição após deteção de irregularidades ( 4 / dia -> hora seguinte 15m em 15m).
          - T - 50%
          - PACO2 - 25% 
          - PA02 - 25%
          - RR - 50%
  
    - **Testes Fisicos**
      - Teste Calibração! (70%)
      - Último teste realizado de cada tipo (Atribui-se uma pontuação, qual a escala (?))
        - Fórmula que dado um input de cada componente dos testes fisicos dará um número que expressa o quão bem sucedido foi esse teste (?)
        - Média aritmética dos dois.
  
    - **Questionário**
      - Observação do Último Questionário efetuado.
        - Exemplo: Pontuação 12-16 -> Converter para 0-100 -> 75% -> 25%

    Wellness_Value = (Variaveis_Sensor * 0.4) + (TestesFisicos * 0.3) + (Questionario * 0.3) 

