# Problema do Caminho do Cavalo

Implementação de um aplicativo para solucionar o problema do Caminho do Cavalo.

# O que é?

Consiste no problema matemático que envolve a peça do **Cavalo** no tabuleiro de xadrez. A Intenção é descobrir a sequência de movimentos que o mesmo deve percorrer para passar por todas as posições do tabuleiro.

# Solução

Inicialmente são gerados diversas possíveis soluções para o problema. As soluções então são ordenadas para identificar as melhores.

Através da implementação de um algoritmo ''Genético Elitista'' são selecionados os melhores indivíduos e executado um cruzamento entre a seleção (os melhores indivíduos são determinados de acordo com a quantidade máxima de posições que a peça caminhou). Processo é executado até a geração máxima ser alcançada, ou até achar uma solução.

**A fim de execução de testes é possível indicar um percentual de indivíduos para sofrerem mutação.**

**Exemplo de execução:**

* **@param qtdGenotipos** Quantidade de indivíduos que a população terá.
* **@param qtdGeracao** Quantidade de gerações a serem processadas.
* **@param perMutacao** Percentual de mutação de elementos a cada geração.

`Genetica genetica = new Genetica(10000, 30, 0);`

`genetica.execute();`
