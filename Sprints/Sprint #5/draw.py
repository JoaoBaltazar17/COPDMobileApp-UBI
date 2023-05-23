import pandas as pd
import matplotlib.pyplot as plt

# Ler o arquivo .csv usando o pandas e especificar as colunas corretas
data = pd.read_csv(r'Sprints\Sprint #5\TestesPassos\csv\teste_02_joao_rapido.csv', usecols=[0, 4])  # Coluna 1 (índice 0) é o tempo, coluna 5 (índice 4) é a aceleração


# Extrair as colunas de tempo e norma da aceleração
tempo = data.iloc[:, 0]  # Extrair todas as linhas da coluna 0 (tempo)
aceleracao_norma = data.iloc[:, 1]  # Extrair todas as linhas da coluna 1 (norma da aceleração)

# Plotar o gráfico
plt.plot(tempo, aceleracao_norma)
plt.xlabel('Tempo')
plt.ylabel('Norma da Aceleração')
plt.title('Gráfico de Norma da Aceleração em função do Tempo')
plt.grid(True)
plt.show()
