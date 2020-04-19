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

/**
 *
 * @author 'Samuel José Eugênio - https://github.com/samuelgenio'
 */
public class Node {
    
    public Node nextNode;
    
    public Node prevNode;
    
    public int numberFunction;

    public Node(int numberFunction) {
        this.numberFunction = numberFunction;
    }
    
    public int getNodePosition(int indexPosition) {
    
        int retorno = -1;
        
        int adicional = 0;
        
        //estão no canto direito ou esquerdo.
        if(indexPosition % 8 == 0 || (indexPosition -1) % 8 == 0) {
            if(numberFunction == 6 || numberFunction == -6) {
                //posicao lateral direita
                adicional = (indexPosition % 8 == 0) ? (numberFunction > 0 ? 4 : -4) : 0;
                //posicao lateral esquerda
                adicional = ((indexPosition -1) % 8 == 0) ? (numberFunction > 0 ? -4 : +4) : 0;
            } else if(numberFunction == 15 || numberFunction == -15) { 
                //posicao lateral direita
                adicional = (indexPosition % 8 == 0) ? (numberFunction > 0 ? 2 : -2) : 0;
                //posicao lateral esquerda
                adicional = ((indexPosition -1) % 8 == 0) ? (numberFunction > 0 ? -2 : +2) : 0;
            }
        }
        
        
        
        retorno = indexPosition + numberFunction + adicional;
        
        return retorno;
        
    }
}
