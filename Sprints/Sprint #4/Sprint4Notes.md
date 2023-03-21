# Sprint 4 - Notes 

# Tarefas

- [ ]  1. Estudar FHIR
   
- [ ]  2. Adicionar conteúdo ao relatório


--------------------------

## 1. Fast Healthcare Interoperability Resources (FHIR)


### 1.1 O que é? (**DEF**) / Funcionalidades que pode oferecer (**F**)

 - **DEF** - É um padrão de __interoperabilidade__ de dados de saúde que foi desenvolvido para facilitar o compartilhamento de informações de saúde entre sistemas de informação de saúde.
 - **DEF** - O FHIR é projetado para ser fácil de implementar e usar, e permite que os desenvolvedores de aplicativos de saúde acessem facilmente informações de saúde em tempo real a partir de uma variedade de fontes de dados.
 - **DEF** - Permitir a troca de dados de saúde de maneira rápida e fácil.
 - **F** - Dados podem ser armazenados em um formato FHIR e compartilhados com o sistema de saúde do      paciente ou com um profissional de saúde específico, permitindo que eles possam acessar e analisar as informações para ajustar o tratamento, fornecer orientações mais precisas e personalizadas, e monitorar a progressão da doença ao longo do tempo.
 - **F** - Acessar informações sobre medicamentos, dosagens e interações medicamentosas, permitindo que os pacientes tenham uma melhor compreensão dos medicamentos que estão tomando e como eles afetam sua condição.
 - **F** - Uso do FHIR permite também a integração com dispositivos médicos, como oxímetros de pulso e medidores de fluxo de ar, permitindo que os pacientes monitorem seus níveis de oxigênio e fluxo de ar em tempo real


### 1.2 **FHIR** em uma aplicacão móvel relacionada com a DPOC

1. Identificar os **recursos** FHIR relevantes para a gestão da DPOC.
   
   1.1 Exemplos:

   * Paciente: Este recurso representa um paciente e contém informações como nome, data de nascimento e informações de contato.

   * Condição: Este recurso representa uma condição que o paciente possui, como a DPOC. Ele contém informações como a data do diagnóstico, a gravidade e os sintomas relacionados.

   * Observação: Este recurso representa uma observação ou medição relacionada à DPOC do paciente, como os resultados dos testes de função pulmonar ou os níveis de saturação de oxigênio.

   * MedicationRequest: Este recurso representa uma solicitação de prescrição de medicamentos ao paciente para a gestão da DPOC. Ele contém informações como o nome do medicamento, dosagem e instruções.

   * ~~DiagnosticReport~~ : Este recurso representa um relatório de testes diagnósticos relacionados à DPOC do paciente, como tomografia computadorizada ou teste de função pulmonar.

   Ao identificar os recursos FHIR relevantes para a gestão da DPOC, pode-se garantir que a base de dados da aplicação incluí todas as informações necessárias para oferecer suporte na gestão da DPOC e permitir a interoperabilidade com outros sistemas de assistência médica. 

2. Fontes de Dados.
 
   * Uma fonte comum de dados de saúde é um sistema de saúde eletrônico (EHR), que contém informações médicas  dos pacientes, como histórico médico, prescrições e resultados de testes. A aplicação móvel pode acessar essas informações usando APIs FHIR disponíveis no EHR.

  * Outra fonte de dados são os dispositivos vestíveis, como medidores de oxigênio no sangue ou monitores de respiração. A aplicação móvel pode se conectar a esses dispositivos por meio de Bluetooth ou outra tecnologia semelhante e coletar informações de saúde em tempo real.

  * Além disso, a aplicação móvel pode usar outros aplicativos de saúde que já coletam informações de saúde dos pacientes, como aplicativos para rastreamento de sintomas ou gerenciamento de medicamentos. A aplicação pode obter essas informações usando APIs FHIR disponíveis nesses aplicativos.


## 2. Conteúdo Adicionado ao Relatório

 - Cap. Introdução:
   - Correção de conceitos.
   - Completar os objetivos.
 - Cap. Estado da Arte:
   - Incorporação da tabela das aplicações relacionadas à DPOC


-----------------------
// FHIR (Fast Healthcare Interoperability Resources) é um padrão para troca de informações de saúde eletrônica entre sistemas de saúde. Uma aplicação móvel relacionada com a DPOC (Doença Pulmonar Obstrutiva Crônica) pode utilizar o FHIR para intercambiar informações de saúde do paciente com outros sistemas de saúde. Abaixo estão as etapas básicas para implementar uma aplicação móvel que utilize o FHIR para a DPOC:

Definir as necessidades da aplicação: Determine quais recursos do FHIR a aplicação precisará para atender às necessidades dos usuários da aplicação. Por exemplo, a aplicação pode precisar de recursos de pacientes, observações e medicamentos.

Identificar as fontes de dados: Determine as fontes de dados que a aplicação usará para obter as informações de saúde dos pacientes. Isso pode incluir sistemas de saúde, dispositivos vestíveis ou outros aplicativos de saúde.

Implementar a autenticação e autorização: Implemente mecanismos de autenticação e autorização para garantir que apenas usuários autorizados possam acessar as informações de saúde dos pacientes.

Desenvolver os recursos da aplicação: Desenvolva a aplicação móvel para interagir com os recursos do FHIR necessários. Por exemplo, a aplicação pode permitir que os usuários visualizem suas observações de saúde ou registrem seus medicamentos.

Testar a aplicação: Realize testes rigorosos para garantir que a aplicação esteja funcionando corretamente e que as informações de saúde dos pacientes estejam sendo tratadas de forma segura e precisa.

Lançar a aplicação: Após testar e validar a aplicação, lance-a para os usuários finais.

Monitorar a aplicação: Monitore a aplicação para garantir que ela esteja funcionando corretamente e que esteja sendo usada pelos usuários. Faça ajustes e melhorias conforme necessário para garantir que a aplicação atenda às necessidades dos usuários e esteja em conformidade com as normas de segurança e privacidade de dados.



  


