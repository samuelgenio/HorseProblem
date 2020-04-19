/*
 * Copyright (C) 2020 Asconn
 *
 * This file is part of HorseProblem.
 * HorseProblem is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * HorseProblem is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <https://www.gnu.org/licenses/>
 */
package br.com.samuka.horseproblem.classes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author 'Samuel José Eugênio - https://github.com/samuelgenio'
 */
public class Genetica {

    private final int QTD_CELULA = 64;

    /**
     * Quantidade de gerações.
     */
    private int qtdGeracao;

    /**
     * Quantidade de individuos da população.
     */
    private int qtdGenotipos;

    /**
     * Genotipos utilizados.
     */
    Integer[][] genotipos;

    /**
     * Fenotipos dos genotipos atuais.
     */
    Integer[] fenotipos;

    File file = new File("result.txt");

    FileWriter writer;

    private int geracaoAtual;

    List<Integer> listIndexesRoleta;

    private double qtdIndividuosMutacao;

    private Node[] nodes;

    /**
     *
     * @param qtdGenotipos Quantidade de indivíduos que a população terá.
     * @param qtdGeracao Quantidade de derações serão processadas.
     * @param perMutacao Percentual de mutação de cada geração.
     */
    public Genetica(int qtdGenotipos, int qtdGeracao, double perMutacao) {
        this.qtdGenotipos = qtdGenotipos;
        this.qtdIndividuosMutacao = qtdGenotipos * perMutacao / 100;
        if (qtdGeracao != -1) {
            this.qtdGeracao = qtdGeracao;
        }
        try {
            writer = new FileWriter(file);
        } catch (IOException ex) {
        }

    }

    /**
     * Executa o processo da criação da geração.
     */
    public void execute() {

        initializeNodes();

        generatePopulation();

        geracaoAtual = 0;

        while (geracaoAtual < qtdGeracao - 1) {

            calculateFenotipos();

            boolean isResult = false;
            int i = 0;
            while (i < fenotipos.length) {
                if (fenotipos[i] == 63) {
                    System.err.println("Resposta encontrada na geração [" + (geracaoAtual + 1) + "]: " + i);
                    isResult = true;
                }
                i++;
            }

            if (isResult) {
                break;
            }

            nextGeneration();

            geracaoAtual++;
        }

        calculateFenotipos();

        int i = 0;

        try {

            String resultLine = "";
            String resultGenotipo = "";
            for (Integer[] genotipo : genotipos) {
                resultGenotipo += "[";
                int cont = 0;
                for (Integer integer : genotipo) {
                    resultGenotipo += integer;
                    if (cont < genotipo.length) {
                        resultGenotipo += ", ";
                    }
                    cont++;
                }

                resultGenotipo += "]";
                resultLine += String.valueOf(fenotipos[i]);
                if (i + 1 < genotipos.length) {
                    resultLine += " - ";
                    resultGenotipo += " - ";
                }

                i++;
            }

            writer.write((geracaoAtual + 1) + "° Geração[G] -> ");
            writer.write(resultGenotipo + "\r\n");
            writer.write((geracaoAtual + 1) + "° Geração[F] -> ");
            writer.write(resultLine);
            writer.flush();
        } catch (IOException ex) {
        }

    }

    private void nextGeneration() {

        QuickSort.genotipo = genotipos;
        boolean allSame = true;
        int i = 0;

        try {

            String resultLine = "";
            String genotipoLine = "";

            for (Integer[] genotipo : genotipos) {
                genotipoLine += "[";
                int cont = 0;
                for (Integer integer : genotipo) {
                    genotipoLine += integer;
                    if (cont < genotipo.length) {
                        genotipoLine += ", ";
                    }
                    cont++;
                }

                genotipoLine += "]";

                resultLine += fenotipos[i];

                if (i + 1 < genotipos.length) {
                    resultLine += " - ";
                    genotipoLine += " - ";
                }

                allSame = allSame && fenotipos[0].equals(fenotipos[i]);

                i++;
            }

            writer.write((geracaoAtual + 1) + "º Geração[G] -> ");
            writer.write(genotipoLine + "\r\n");
            writer.write((geracaoAtual + 1) + "º Geração[F] -> ");
            writer.write(resultLine + "\r\n");
            writer.flush();
        } catch (IOException ex) {
        }

        if (allSame) {
            System.out.println("todos iguais");
            System.exit(0);
        }

        QuickSort.order(fenotipos, 0, fenotipos.length - 1);

        genotipos = QuickSort.genotipo;

        Integer[][] nextGeneration = new Integer[qtdGenotipos / 2][QTD_CELULA];

        getGenotiposElitismo(nextGeneration);

        genotipos = new Integer[genotipos.length][QTD_CELULA];

        double qtdMutacaoAtual = qtdIndividuosMutacao;

        i = 0;
        int indexGenotiposAdded = 0;
        while (i < nextGeneration.length) {

            boolean isMutacao = false;

            if (qtdMutacaoAtual > 0) {

                if (qtdMutacaoAtual < 1) {
                    isMutacao = Math.random() * 1 < qtdMutacaoAtual;
                    qtdMutacaoAtual = isMutacao ? - 1 : -0;
                } else {
                    isMutacao = true;
                    qtdMutacaoAtual--;
                }

            }

            int indexCut = (int) (Math.random() * 64);

            indexCut = (indexCut == 0) ? 1 : indexCut;
            Integer[][] sons = getSon(nextGeneration[i], nextGeneration[i + 1], isMutacao, indexCut);

            genotipos[indexGenotiposAdded++] = nextGeneration[i];
            genotipos[indexGenotiposAdded++] = nextGeneration[i + 1];
            genotipos[indexGenotiposAdded++] = sons[0];
            genotipos[indexGenotiposAdded++] = sons[1];

            i = i + 2;
        }
    }

    /**
     * Produz a próxima geração.
     *
     * @param ancient1 Ancestral 1
     * @param ancient2 Ancestral 2
     * @param isMutacao Indica se os filhos sofreram mutação.
     * @return Integer[][] com os dois filhos gerados
     */
    private Integer[][] getSon(Integer[] ancient1, Integer[] ancient2, boolean isMutacao, int indexCut) {

        Integer[] son1 = new Integer[QTD_CELULA];
        Integer[] son2 = new Integer[QTD_CELULA];

        int localCut = indexCut;

        int j = 0;
        int indexInsert1 = j;
        int indexInsert2 = j;
        while (j < ancient1.length) {

            if (j == localCut) {
                Integer[] ancientTroca = ancient1.clone();
                ancient1 = ancient2.clone();
                ancient2 = ancientTroca.clone();
            }

            int genotipoSon1 = -1;
            int genotipoSon2 = -1;

            if (j >= localCut) {

                //filho 1
                boolean exists = exists(ancient1[indexInsert1], son1);
                indexInsert1 = exists ? -1 : j;
                while (exists) {
                    indexInsert1++;
                    exists = exists(ancient1[indexInsert1], son1);
                }

                genotipoSon1 = ancient1[indexInsert1];

                //filho 2
                exists = exists(ancient2[indexInsert2], son2);
                indexInsert2 = exists ? -1 : j;
                while (exists) {
                    indexInsert2++;
                    exists = exists(ancient2[indexInsert2], son2);
                }

                genotipoSon2 = ancient2[indexInsert2];

            } else {
                genotipoSon1 = ancient1[j];
                genotipoSon2 = ancient2[j];
                indexInsert1 = ++indexInsert2;

            }

            son1[j] = genotipoSon1;
            son2[j] = genotipoSon2;

            j++;

        }

        if (isMutacao) {

            int index = new Random().nextInt(son1.length - 1) + 1;

            if (Math.random() * 1 > 0.5) {
                son1[index] = son1[index] > 0 ? 0 : 1;
            } else {
                son2[index] = son2[index] > 0 ? 0 : 1;
            }
        }

        return new Integer[][]{son1, son2};
    }

    /**
     *
     * @param number
     * @param arrFind
     * @param indexAtual
     * @return
     */
    private boolean exists(int number, Integer[] arrFind) {

        boolean retorno = false;

        int count = 0;
        while (count < QTD_CELULA) {

            if (arrFind[count] == null) {
                break;
            }

            if (arrFind[count] == number) {
                retorno = true;
                break;
            }
            count++;
        }

        return retorno;
    }

    /**
     * Obtém somente os genotipos mais fortes.
     */
    private void getGenotiposElitismo(Integer[][] nextGeneration) {

        int half = genotipos.length / 2;

        int count = 0;
        while (half > 0) {
            nextGeneration[count] = genotipos[--half];
            count++;
        }
    }

    /**
     * Calcula os fenotipos dos genotipos. O fenotipo é a quantidade de rainhas
     * que não se veem. Quanto maior o número mais próximo da solução se está.
     */
    private void calculateFenotipos() {

        fenotipos = new Integer[genotipos.length];

        int i = 0;
        for (Integer[] genotipo : genotipos) {

            int fenotipo = 0;
            int j = 0;

            while (j < genotipo.length - 1) {

                fenotipo += seeAnotherNode(j, i) ? 1 : 0;
                j++;
            }

            fenotipos[i] = fenotipo;
            i++;
        }
    }

    /**
     * Retorna true caso a rainha vê outra.
     *
     * @param index
     * @return
     */
    private boolean seeAnotherNode(int index, int indexGenotipoVez) {//int[][] tabuleiro
        boolean retorno;

        List<Integer> nodesPossibles = new ArrayList<>();

        int line = 1;
        int col = 0;
                
        int numberPos = genotipos[indexGenotipoVez][index];
    
        while(numberPos > 0) {
            
            if(numberPos > 8) {
                numberPos-= 8;
                line++;
            } else {
                numberPos--;
                col++;
            }
        }
        
        if ((line >= 3 && line <= 6) && (col >= 3 && col <= 6)) {
            nodesPossibles.add(10);
            nodesPossibles.add(6);
            nodesPossibles.add(15);
            nodesPossibles.add(17);
        } else {

            if (col == 1) {
                nodesPossibles.add(10);
                nodesPossibles.add(17);
                if(line >= 2) {
                    nodesPossibles.add(6);
                }
                
                if (line >= 7) {
                    nodesPossibles.add(15);
                }
            }
            
            if (col == 8) {
                nodesPossibles.add(6);
                nodesPossibles.add(15);
                if(line >= 2) {
                    nodesPossibles.add(10);
                }
                if (line >= 7) {
                    nodesPossibles.add(17);
                }
            }
            
            if((col == 1 || col == 8) && (line >= 3 && line <= 6)) {
                nodesPossibles.add(15);
                nodesPossibles.add(17);
            }
            
            if (col >= 2 && col <= 7) {
                nodesPossibles.add(15);
                nodesPossibles.add(10);
                nodesPossibles.add(17);
            }

            if ((col >= 3 && col <= 6) || line > 1) {
                nodesPossibles.add(6);
            }

        }

        int indexDestino = index + 1;

        int calcFunction = Math.abs(genotipos[indexGenotipoVez][indexDestino] - genotipos[indexGenotipoVez][index]);

        retorno = nodesPossibles.contains(calcFunction);

        return retorno;
    }

    /**
     * Cria a população.
     */
    private void generatePopulation() {

        genotipos = new Integer[qtdGenotipos][QTD_CELULA];

        Vector vec = new Vector<Integer>();
        for (int i = 1; i < 65; i++) {
            vec.add(i);
        }

        try {
            int i = 0;
            while (i < qtdGenotipos) {

                genotipos[i] = getGenotipo((Vector)vec.clone());
                
                /*genotipos[i] = new Integer[]{
                    1, 18, 12, 27, 33, 50, 60, 43,
                    37, 54, 64, 47, 32, 15, 21, 6,
                    23, 8, 14, 29, 39, 56, 62, 45,
                    35, 52, 58, 41, 26, 9, 3, 20,
                    5, 22, 16, 31, 48, 63, 53, 38,
                    44, 59, 49, 34, 28, 11, 17, 2,
                    19, 4, 10, 25, 42, 57, 51, 36,
                    46, 61, 55, 40, 30, 13, 7, 24
                };*/
                //caminho perfeito.

                i++;
            }
        } catch (ParseException e) {
        }
    }

    /**
     * Obtém um genotipo
     *
     * @param vec Vector com a quantidade de genotipos
     * @return
     * @throws ParseException
     */
    private Integer[] getGenotipo(Vector vec) throws ParseException {

        Integer[] genotipo = new Integer[QTD_CELULA];
        int cont = 0;

        int lastPosition = 24;//(int)(Math.random() * QTD_CELULA-1);

        genotipo[cont] = lastPosition;
        vec.remove(new Integer(lastPosition));//remover objeto e não o indice.
        cont++;

        while (cont < QTD_CELULA) {

            boolean sucess;
            int indexSet = -1;
            try {

                indexSet = getNexPosition(lastPosition, vec);
                sucess = indexSet != -1;

            } catch (Exception e) {
                sucess = false;
                e.printStackTrace();
            }

            if (!sucess) {
                indexSet = (int) (Math.random() * vec.size());
                indexSet = (int) vec.get(indexSet);
            }

            vec.remove(new Integer(indexSet));
            genotipo[cont] = indexSet;
            lastPosition = indexSet;

            cont++;
        }

        return genotipo;

    }

    private int getNexPosition(int indexAtual, Vector vec) {

        int retorno = -1;

        int index = (int) (Math.random() * 7);

        Node nodeStart = nodes[index];
        Node node = nodes[index];

        int line = 1;
        int col = 1;
        for (int i = 1; i < QTD_CELULA + 1; i++) {

            if (i == indexAtual) {
                break;
            }

            if (i % 8 == 0) {
                line++;
                col = 0;
            }
            col++;
        }

        List<Integer> nodesPossibles = new ArrayList<>();

        if ((line >= 3 && line <= 6) && (col >= 3 && col <= 6)) {
            nodesPossibles.add(10);
            nodesPossibles.add(6);
            nodesPossibles.add(15);
            nodesPossibles.add(17);
        } else {

            if (col == 1) {
                nodesPossibles.add(10);
                nodesPossibles.add(17);
                if(line >= 2) {
                    nodesPossibles.add(6);
                }
                
                if (line >= 7) {
                    nodesPossibles.add(15);
                }
            }
            
            if (col == 8) {
                nodesPossibles.add(6);
                nodesPossibles.add(15);
                if(line >= 2) {
                    nodesPossibles.add(10);
                }
                if (line >= 7) {
                    nodesPossibles.add(17);
                }
            }
            
            if((col == 1 || col == 8) && (line >= 3 && line <= 6)) {
                nodesPossibles.add(15);
                nodesPossibles.add(17);
            }
            
            if (col >= 2 && col <= 7) {
                nodesPossibles.add(15);
                nodesPossibles.add(10);
                nodesPossibles.add(17);
            }

            if ((col >= 3 && col <= 6) || line > 1) {
                nodesPossibles.add(6);
            }

        }

        do {

            if (nodesPossibles.contains(node.numberFunction)) {
                int nextPos = node.getNodePosition(indexAtual);

                if (vec.contains(nextPos)) {
                    retorno = nextPos;
                    break;
                }
            }

        } while (node != nodeStart);

        return retorno;
    }

    /**
     * Inicializa os Nodes com função para obter um proximo ponto válido.
     */
    private void initializeNodes() {

        int i = 0;
        nodes = new Node[8];
        nodes[i++] = new Node(-17);
        nodes[i++] = new Node(-10);
        nodes[i++] = new Node(-15);
        nodes[i++] = new Node(-6);

        nodes[i++] = new Node(+17);
        nodes[i++] = new Node(+10);
        nodes[i++] = new Node(+15);
        nodes[i++] = new Node(+6);

        nodes[0].nextNode = nodes[1];
        nodes[0].prevNode = nodes[7];

        nodes[1].nextNode = nodes[2];
        nodes[1].prevNode = nodes[0];

        nodes[2].nextNode = nodes[3];
        nodes[2].prevNode = nodes[1];

        nodes[3].nextNode = nodes[4];
        nodes[3].prevNode = nodes[2];

        nodes[4].nextNode = nodes[5];
        nodes[4].prevNode = nodes[3];

        nodes[5].nextNode = nodes[6];
        nodes[5].prevNode = nodes[4];

        nodes[6].nextNode = nodes[7];
        nodes[6].prevNode = nodes[5];

        nodes[7].nextNode = nodes[0];
        nodes[7].prevNode = nodes[6];

    }

}
