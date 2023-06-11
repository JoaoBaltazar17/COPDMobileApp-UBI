# Meeting 5 (29/05/2023)

## 1. Wellness Value
  - Escala (0 - ?)
    - Valores Intermediários:
      - Categorias: 
        - **Sem dados suficientes para avaliação**
          - Feedback de Cores: Branco / Cinzento
          - Dar esta informação quando o doente abre a app pela 1ºvez.
          - Disponibilizar informação de como ter valores suficientes para avaliação, através de um botão de ajuda ao lado do wellness.
        - **[0, 30]  - Your COPD is not controlled!**
          - Feedback de Cores: Vermelho
        - **[30, 65] - Your COPD is medium controlled!**
          - Feedback de Cores: Amarelo
        - **[65, 100] - Your COPD is absolutely controlled!**
          - Feedback de Cores: Verde
  
  - Fórmula (Praticial): 
    - **Variáveis Sensor** 
      - Análise de valores recolhidos nas últimas (24h (?)).
        - Tratamento destes resultados (detetar valores anormais, quais são os valores anormais (?) )
        - Peso de cada variável (Média Aritmética ou Ponderada (?) )
  
    - **Testes Fisicos**
      - Último teste realizado de cada tipo (Atribui-se uma pontuação, qual a escala (?))
        - Fórmula que dado um input de cada componente dos testes fisicos dará um número que expressa o quão bem sucedido foi esse teste (?)
        - Média aritmética dos dois.
  
    - **Questionário**
      - Desempenho do último questionário esfetuado (Pontuação!)

    Wellness_Value = (Variaveis_Sensor * 0.4) + (TestesFisicos * 0.3) + (TestesFisicos * 0.3) 