/*
 * This file is part of ELKI:
 * Environment for Developing KDD-Applications Supported by Index-Structures
 *
 * Copyright (C) 2019
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

import static org.junit.Assert.*;

import org.junit.Test;

import elki.database.ids.DBIDRange;
import elki.database.ids.DBIDUtil;

/**
 * Validate {@link KuhnMunkresWongAccuracy} based measures with the SkLearn example
 *
 * @author Robert Gehde
 */
public class KuhnMunkresWongAccuracyTest extends AbstractClusterEvaluationTest {
  /**
   * Test {@link KuhnMunkresWongAccuracy} with SkLearn example.
   */
  @Test
  public void testKMWAcc() {
    DBIDRange ids = DBIDUtil.generateStaticDBIDRange(SKLEARNA.length);
    ClusterContingencyTable cc = new ClusterContingencyTable(true, false, makeClustering(ids.iter(), SKLEARNA), makeClustering(ids.iter(), SKLEARNB));
    KuhnMunkresWongAccuracy kmwacc = new KuhnMunkresWongAccuracy(cc);
    
    assertEquals("KMW Accuracy not as expected", 12./17., kmwacc.getAccuracy(), 1e-15);
  }
}
