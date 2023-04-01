# Sprint 6 - Notes 

# Order of The Week

- [x]  1. Estado da Arte (Atualização da tabela e respetivos conteúdos)

- [ ]  2. Abordagem aos testes fisicos: (6MWT) 6 minute walk test / (1-MSTST) 1 minute sit to stand
   
- [ ]  3. Exploração da aplicação do Count Steps 
  - [ ]  Imprimir valores de acelerometria. 
  - [ ]  Elaborar um pequeno gráfico dos valores num espaço de tempo.
  - [ ]  Finalizar protótipo.
  
--------------------------

## 2. Testes Fisícos

## 2.1. 6 Minute Walk Test (6MWT)

**Definição**: O teste mede a distância que um paciente pode caminhar em 6 minutos em uma superfície plana e dura, como um corredor de hospital ou uma pista. Além disso, acompanha-se a medição da distância, com a mediação da saturação de oxigênio e da frequência cardíaca. 


**Utilidade** : 

* Determinar a gravidade da DPOC e acompanhar as alterações na capacidade de exercício ao  longo do tempo.
  
* Avaliação da resposta à terapia: O 6MWT pode ser usado para monitorar a eficácia de intervenções como a reabilitação pulmonar ou farmacoterapia para melhorar a capacidade de exercício e o estado funcional.
  
* Estudos demonstraram que a distância percorrida durante o 6MWT é um prenunciador de mortalidade e hospitalização em pacientes com DPOC e pode ajudar a identificar pacientes com maior risco de resultados adversos.
  
* Identificação de comorbidades (qualquer patologia independente e adicional a uma outra existente): O 6MWT pode ser usado para avaliar o impacto de comorbidades como insuficiência cardíaca ou hipertensão pulmonar na capacidade de exercício e para orientar a gestão dessas condições.

**Limitações**: 

* Não fornece informações sobre os mecanismos fisiológicos específicos subjacentes à limitação do exercício na DPOC.
  
* Não ser sensível o suficiente para detectar pequenas mudanças na capacidade de exercício.
  
* O teste pode não ser apropriado para pacientes incapazes de caminhar por 6 minutos ou que tenham comorbidades significativas que possam confundir os resultados.


**(I) - Integração na app móvel // (D) - Desenvolvimento**:

* Integração na app móvel:
  * Desenvolver uma aplicação móvel que seja capaz de medir a distância percorrida pelo paciente e registrar dados como frequência cardíaca, oximetria de pulso e sintomas percebidos.
* 
  * Definir um protocolo para realizar o teste (indicar as diretrizes estabelecidas para o 6MWT e incluir instruções claras para o paciente).
* 
  * Aviso: É importante ressaltar que a utilização de uma aplicação móvel com acelerômetro para realizar o 6MWT requer validação clínica rigorosa antes de ser amplamente utilizada em pacientes com COPD. Além disso, a utilização de uma aplicação móvel não substitui a avaliação médica e o acompanhamento clínico regular.

* Desenvolvimento:
  * Medir a distância percorrida pelo paciente: 
    * (?) Através do acelerômetro ou GPS.
* 
  * Avaliar a fiabilidade do teste, i. e.:
    * Obter os valores da precisão do sensor, da qualidade da calibração do dispositivo e da presença de movimentos bruscos ou mudanças de direção e segundo estes, dar a conhecer ao doente a % de fiabilidade do mesmo.


**Processamento dos dados na página web**:

* Distância percorrida: A distância percorrida durante o teste é um indicador importante da capacidade funcional do paciente. A distância caminhada pode ser comparada com valores de referência para a idade, sexo e altura do paciente, para avaliar a gravidade da disfunção pulmonar.
  
* Frequência cardíaca: A frequência cardíaca durante o teste é um indicador do esforço físico do paciente. A frequência cardíaca pode ser utilizada para monitorar o esforço do paciente durante o teste e avaliar a eficácia de intervenções terapêuticas, como a administração de broncodilatadores.
  
* Oximetria de pulso: A oximetria de pulso durante o teste é um indicador da saturação de oxigênio do paciente e pode ser utilizado para monitorar a resposta do paciente à atividade física. A oximetria de pulso pode ser utilizada para avaliar a necessidade de oxigênio suplementar durante o teste e para ajustar as doses de oxigênio prescritas.

* Sintomas percebidos: Os sintomas percebidos pelo paciente durante o teste são uma importante medida subjetiva do esforço físico. Os sintomas percebidos podem ser utilizados para monitorar a tolerância do paciente à atividade física e para avaliar a eficácia das intervenções terapêuticas.


## 2.2. 1 Minute Sit to Stand (1-MSTST) 

**Definição**: Teste que avalia a capacidade funcional de uma pessoa em realizar movimentos repetitivos de sentar e levantar de uma cadeira em um minuto. 
- Avalia:
  - a força muscular dos membros inferiores, o equilíbrio e a capacidade funcional em idosos e em pacientes com doenças crônicas, como a doença pulmonar obstrutiva crônica (COPD). 
  - a fadiga percebida pelo paciente durante o teste utilizando uma escala de Borg, que varia de 0 a 10, em que 0 representa "nenhuma fadiga" e 10 representa "fadiga máxima"


**Utilidade**: 

* Avaliar a força corporal inferior e a aptidão funcional em indivíduos.
  
* Fornecer informações valiosas sobre a capacidade do indivíduo de realizar atividades do dia a dia.
  
* Rastrear mudanças na capacidade funcional ao longo do tempo e para monitorar a eficácia de programas de reabilitação.


**Limitações:**

* Pode não ser apropriado para indivíduos com certas condições médicas, como artrite grave ou dor nas articulações, pois pode causar desconforto ou exacerbar seus sintomas.
  
* Pode não refletir com precisão a capacidade funcional geral de um indivíduo, pois avalia apenas a força e a resistência da parte inferior do corpo em uma tarefa específica.
  
* O teste pode ser influenciado por outros fatores, como altura, peso e idade, que podem afetar a capacidade do indivíduo de realizar o teste.


**(I) - Integração na app móvel // (D) - Desenvolvimento :**

* Integração na app móvel:
  * Contar o número de vezes que se fez o movimento (sentar e levantar).
  * Tempo necessário (cumpriu antes do tempo (quanto em especifico) / ultrapassou o tempo).
  * Definir um protocolo para realizar o teste (indicar as diretrizes estabelecidas para o 6MWT e incluir instruções claras para o paciente).
  
  * Aviso: A precisão dos resultados pode variar e pode não ser confiável o suficiente para fins de avaliação ou monitoramento. A melhor maneira de medir o número de repetições do teste é com a ajuda de um observador humano experiente.

* Desenvolvimento:
  * Detetar o movimento de sentar e levantar da cadeira:
    * Através do acelerómetro.
  * Crónometro.
  * O acelerômetro pode ser colocado na parte inferior das costas ou no bolso da pessoa sendo testada, de  
   forma que o telefone fique na posição vertical, de modo a medir as mudanças de movimento no eixo z.
  * Avaliar a fiabilidade do teste, i. e.:
    * Obter os valores da precisão do sensor, da qualidade da calibração do dispositivo e da presença de movimentos bruscos ou mudanças de direção e segundo estes, dar a conhecer ao doente a % de fiabilidade do mesmo.


**Processamento dos dados na página web**:

* Número de repetições: Indicador de desempenho físico.

* Cronómetro: Tempo que demorou a completar determinadas repetições.

* Avaliar a capacidade da pessoa em realizar atividades diárias que exigem levantar-se e sentar-se repetidamente.
  
* Monitorar o progresso de um programa de treinamento físico e para avaliar o impacto de intervenções terapêutica