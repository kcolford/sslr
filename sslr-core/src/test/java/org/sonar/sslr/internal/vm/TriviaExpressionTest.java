/*
 * SonarSource Language Recognizer
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.sslr.internal.vm;

import com.sonar.sslr.api.Trivia.TriviaKind;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class TriviaExpressionTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_compile() {
    TriviaExpression expression = new TriviaExpression(TriviaKind.COMMENT, new SubExpression(1, 2));
    assertThat(expression.toString()).isEqualTo("Trivia COMMENT[SubExpression]");
    Instruction[] instructions = expression.compile(new CompilationHandler());
    assertThat(instructions).isEqualTo(new Instruction[] {
      Instruction.call(2, expression),
      Instruction.jump(5),
      Instruction.ignoreErrors(),
      SubExpression.mockInstruction(1),
      SubExpression.mockInstruction(2),
      Instruction.ret()
    });
  }

  @Test
  public void should_implement_Matcher() {
    TriviaExpression expression = new TriviaExpression(TriviaKind.COMMENT, mock(ParsingExpression.class));
    // Important for AstCreator
    assertThat(expression.getTriviaKind()).isSameAs(TriviaKind.COMMENT);
  }

}