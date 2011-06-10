/*
 * Copyright (C) 2010 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */

package com.sonar.sslr.impl.matcher;

import static com.sonar.sslr.impl.matcher.GrammarFunctions.Advanced.isFalse;
import static com.sonar.sslr.impl.matcher.GrammarFunctions.Advanced.isTrue;
import static com.sonar.sslr.impl.matcher.GrammarFunctions.Standard.or;
import static com.sonar.sslr.impl.matcher.HamcrestMatchMatcher.match;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class OrMatcherTest {

  @Test
  public void ok() {
    assertThat(or(isFalse(), isTrue()), match("one"));
    assertThat(or(isTrue(), isFalse()), match("one"));
    assertThat(or(isFalse(), isFalse()), not(match("one")));
  }
  
  @Test
  public void testToString() {
  	assertEquals(or("(").toString(), "or");
  }

}
