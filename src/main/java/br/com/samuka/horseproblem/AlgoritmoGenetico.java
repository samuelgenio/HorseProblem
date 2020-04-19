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
package br.com.samuka.horseproblem;

import br.com.samuka.horseproblem.classes.Genetica;

/**
 *
 * @author 'Samuel José Eugênio - https://github.com/samuelgenio'
 */
public class AlgoritmoGenetico {

    public static void main(String[] args) {
        
        Genetica genetica = new Genetica(1000, 10, 0);
        genetica.execute();
        
    }
    
}
