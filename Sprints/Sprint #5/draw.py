import pandas as pd
import matplotlib.pyplot as plt


df = pd.read_csv('dados_aceleracao.csv', header=None, names=['tempo', 'aceleracao'])

plt.scatter(df['tempo'], df['aceleracao'])
plt.xlabel('Tempo (ms)')
plt.ylabel('Aceleração (m/s²)')
plt.title('Gráfico de dispersão da aceleração em função do tempo')
plt.show()

