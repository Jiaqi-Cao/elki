/*
 * This file is part of ELKI:
 * Environment for Developing KDD-Applications Supported by Index-Structures
 * 
 * Copyright (C) 2020
 * ELKI Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package elki.evaluation.clustering;

import java.util.Arrays;

import elki.utilities.datastructures.KuhnMunkresWong;

/**
 * A cluster evaluation for accuracy using KuhnMunkresWong algorithm for cluster
 * matching
 * 
 * @author Robert Gehde
 *
 */
public class KuhnMunkresWongAccuracy {

  /**
   * accuracy calculated with Kuhn Munkres Wong
   */
  protected double accuracy;

  /**
   * Calculate cluster accuracy with the Kuhn Munkres Wong Algorithm for minimal
   * cost matching
   *
   * @param table
   */
  public KuhnMunkresWongAccuracy(ClusterContingencyTable table) {
    int[][] cont = table.contingency;
    double[][] costs;
    final int rowlen = table.size1, collen = table.size2;
    // create negative, correctly ordered version for Kuhn Munkres Wong
    if(collen >= rowlen) {
      costs = new double[rowlen][collen];
      for(int i = 0; i < rowlen; i++) {
        for(int j = 0; j < collen; j++) {
          costs[i][j] = -cont[i][j];
        }
      }
    }
    else {
      costs = new double[collen][rowlen];
      for(int i = 0; i < rowlen; i++) {
        for(int j = 0; j < collen; j++) {
          costs[j][i] = -cont[i][j];
        }
      }
    }
    // run Kuhn Munkres Wong
    KuhnMunkresWong kmw = new KuhnMunkresWong();
    int[] chosen = kmw.run(costs);
    System.out.println(Arrays.toString(chosen));
    // cost array is negative, so we need to add the negatives
    double correctAssociations = 0;
    for(int i = 0; i < chosen.length; i++) {
      correctAssociations += -costs[chosen[i]][i];
    }
    
    accuracy = correctAssociations / table.contingency[collen][rowlen];
  }

  /**
   * 
   * @return accuracy calculated with KuhnMunkresWong
   */
  public double getAccuracy() {
    return accuracy;
  }
}
