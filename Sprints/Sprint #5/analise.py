import matplotlib.pyplot as plt

# Dados fornecidos
num_passos_real = [5, 8, 9, 14, 15, 19, 26, 28, 31, 33,]  # Número de passos real
num_passos_app = [7, 11, 13, 11, 18, 17, 26, 24, 27, 25]  # Número de passos contados pela aplicação

# Calculando a diferença entre os passos
diferenca_passos = [r - a for r, a in zip(num_passos_real, num_passos_app)]

# Criando o gráfico
plt.plot(range(len(num_passos_real)), diferenca_passos, marker='o')
plt.xlabel('Indice do Teste')
plt.ylabel('Diferença de Passos')
plt.title('Diferença entre Número de Passos Real e Número de Passos Contados pela App')
plt.xticks(range(len(num_passos_real)))
plt.show()
