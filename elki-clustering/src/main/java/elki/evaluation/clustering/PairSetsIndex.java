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

import net.jafama.FastMath;

/**
 * Pair Sets Index as proposed by Mohammad Resaei and Pasi Fränti
 * "Set Matching Measures for External Cluster Validity"
 * 
 * @author Robert Gehde
 *
 */
public class PairSetsIndex {
  /**
   * sum of chosen pairings
   */
  protected double s = 0;

  /**
   * expected value
   */
  protected double e = 0;

  /**
   * simplified PSI (with e = 1)
   */
  protected double simplifiedPSI = 0;

  /**
   * (s - e) / (max(size1,size2) - e)
   */
  protected double psi = 0;

  public PairSetsIndex(ClusterContingencyTable table) {
    final int rowlen = table.size1, collen = table.size2;
    if(rowlen == collen && rowlen == 1) {
      psi = 1;
      simplifiedPSI = 1;
      return;
    }
    int[][] cont = table.contingency;
    double[][] costs;
    // create negative cost array, correctly ordered for Kuhn Munkres Wong
    if(collen >= rowlen) {
      costs = new double[rowlen][collen];
      for(int i = 0; i < rowlen; i++) {
        for(int j = 0; j < collen; j++) {
          costs[i][j] = -cont[i][j] / (double) FastMath.max(cont[rowlen][j], cont[i][collen]);
        }
      }
    }
    else {
      costs = new double[collen][rowlen];
      for(int i = 0; i < rowlen; i++) {
        for(int j = 0; j < collen; j++) {
          costs[j][i] = -cont[i][j] / (double) FastMath.max(cont[rowlen][j], cont[i][collen]);
        }
      }
    }
    // run Kuhn Munkres Wong
    KuhnMunkresWong kmw = new KuhnMunkresWong();
    int[] chosen = kmw.run(costs);

    // sum up s
    s = 0;
    for(int i = 0; i < chosen.length; i++) {
      s += -costs[i][chosen[i]];
    }

    // calculating e : searching for better sort options
    // copy cont
    int[] firstlevelOrder = new int[rowlen];
    for(int i = 0; i < rowlen; i++) {
      firstlevelOrder[i] = cont[i][collen];
    }

    int[] secondlevelOrder = new int[collen];
    for(int i = 0; i < collen; i++) {
      secondlevelOrder[i] = cont[rowlen][i];
    }

    // sorting
    java.util.Arrays.sort(firstlevelOrder);
    java.util.Arrays.sort(secondlevelOrder);

    // calculating e itself
    e = 0;
    int minlength = FastMath.min(rowlen, collen);
    for(int i = 0; i < minlength; i++) {
      e += (firstlevelOrder[i] * secondlevelOrder[i] / (double) cont[rowlen][collen]) / FastMath.max(firstlevelOrder[i], secondlevelOrder[i]);
    }

    // calculate measures
    simplifiedPSI = s < 1 ? 0 : (s - 1) / (FastMath.max(rowlen, collen) - 1);
    psi = s < e ? 0 : (s - e) / (FastMath.max(rowlen, collen) - e);
  }

  /**
   * 
   * @return the calculated psi value
   */
  public double psi() {
    return psi;
  }

  /**
   * The simplified psi value uses e = 1
   * 
   * @return the calculated simplified psi value
   */
  public double simplifiedPsi() {
    return simplifiedPSI;
  }
}
