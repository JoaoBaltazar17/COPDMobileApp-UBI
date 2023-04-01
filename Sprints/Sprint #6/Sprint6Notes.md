# Sprint 6 - Notes 

# Order of The Week

- [x]  1. Estado da Arte (Atualização da tabela e respetivos conteúdos)

- [ ]  2. Abordagem aos testes fisicos: (6MWT) 6 minute walk test / (1-MSTST) 1 minute sit to stand
   
- [ ]  3. Exploração da aplicação do Count Steps 
  - [ ]  Imprimir valores de acelerometria. 
  - [ ]  Elaborar um pequeno gráfico dos valores num espaço de tempo.
  - [ ]  Finalizar protótipo.
  
--------------------------

## 2. Testes Fisícos - Breve explicação, utilidade na avaliação do estado do paciente, limitações, integração na app móvel e processamento dos dados na página web.

## 2.1. 6 minute walk test (6MWT)

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

**Integração na app móvel**:
* Desenvolver uma aplicação móvel que seja capaz de medir a distância percorrida pelo paciente e registrar dados como frequência cardíaca, oximetria de pulso e sintomas percebidos.
* Defina um protocolo para realizar o teste (indicar as diretrizes estabelecidas para o 6MWT e incluir instruções claras para o paciente).
* Aviso: É importante ressaltar que a utilização de uma aplicação móvel com acelerômetro para realizar o 6MWT requer validação clínica rigorosa antes de ser amplamente utilizada em pacientes com COPD. Além disso, a utilização de uma aplicação móvel não substitui a avaliação médica e o acompanhamento clínico regular.


**Processamento dos dados na página web**:
* Distância percorrida: A distância percorrida durante o teste é um indicador importante da capacidade funcional do paciente. A distância caminhada pode ser comparada com valores de referência para a idade, sexo e altura do paciente, para avaliar a gravidade da disfunção pulmonar.
  
* Frequência cardíaca: A frequência cardíaca durante o teste é um indicador do esforço físico do paciente. A frequência cardíaca pode ser utilizada para monitorar o esforço do paciente durante o teste e avaliar a eficácia de intervenções terapêuticas, como a administração de broncodilatadores.
  
* Oximetria de pulso: A oximetria de pulso durante o teste é um indicador da saturação de oxigênio do paciente e pode ser utilizado para monitorar a resposta do paciente à atividade física. A oximetria de pulso pode ser utilizada para avaliar a necessidade de oxigênio suplementar durante o teste e para ajustar as doses de oxigênio prescritas.

* Sintomas percebidos: Os sintomas percebidos pelo paciente durante o teste são uma importante medida subjetiva do esforço físico. Os sintomas percebidos podem ser utilizados para monitorar a tolerância do paciente à atividade física e para avaliar a eficácia das intervenções terapêuticas.


