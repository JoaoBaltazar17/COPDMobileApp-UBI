# Meeting 4 (29/05/2023)

## 1. CAT 
  O teste CAT tem 8 perguntas, o que é exaustivo e demorado para quem quer ter um diagnóstico rápido... Eu pensei em implementá-lo mas só em parte, ou seja, 4 perguntas.
  ### 1.1 Transformar de 8 para 4 (perguntas escolhidas / pontuação escolhida)
  - 1.1.1 Perguntas Escolhidas (Não houve critério na escolha!)
    - How is your cough today?
    - How is your mucus chest today?
    - How was your sleep last night?
    - How is your condition when you walk up a hill?

  - 1.1.2 Pontuação Escolhida 
    - [0 - 3] - Sem impacto clinicamente relevante.
    - [4 - 7] - Leve
    - [8 - 11] - Moderada
    - [12 - 16] - Grave

## 2. Fórmula
  ### 2.1 Dados disponiveis para elaborar a formúla.
  - 2.1.1 Dados Pessoais
    - Idade
    - copd_severity
    - Relação Peso / Altura
  - 2.1.2 Variáveis Recolhidas pelo Sensor 
    - O que temos destas variáveis é o valor de cada uma associada a uma data!
      - paco2
      - respiratory_rate
      - temperature
      - pao2
  - 2.1.3 Questionário
    - Temos vários questionários registados. Opções de ser implementado na fórmula:
      - consideramos os últimos dois e fazemos a média.
      - consideramos todos e damos um peso mais alto à medida que o questionário é mais recente e fazemos a média ponderada.
  - 2.1.4 Testes fisicos 
    - 6MWT  ->    NºSteps -> Distance (m) / Pulsacão inicial, Pulsacão final
    - 1MSTS ->   CountCycle (SIT STAND) / Pulsacão inicial, Pulsacão final



  ### 2.2 Como atribuir uma pontuação a cada teste / se for necessário?
    Como avaliar o desempenho do doente de 0 a 100? É possível quantificar o desempenho do doente?



## 3. Perfil do Paciente
  Assunto ainda vago, daqui a 1 ou 2 semanas poderemos refletir melhor. Mas a ideia era termos um perfil para depois apresentarmos como caso de uso na apresentação da aplicação.
  



