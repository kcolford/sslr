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
package org.sonar.sslr.benchmarks;

import com.google.common.base.Strings;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.sonar.sslr.internal.vm.CompilationHandler;
import org.sonar.sslr.internal.vm.EndOfInputExpression;
import org.sonar.sslr.internal.vm.Instruction;
import org.sonar.sslr.internal.vm.Machine;
import org.sonar.sslr.internal.vm.OneOrMoreExpression;
import org.sonar.sslr.internal.vm.ParsingExpression;
import org.sonar.sslr.internal.vm.SequenceExpression;
import org.sonar.sslr.internal.vm.StringExpression;
import org.sonar.sslr.internal.vm.ZeroOrMoreExpression;

@State
@BenchmarkMode(Mode.Throughput)
public class OneOrMoreExpressionBenchmark {

  private String input;
  private Instruction[] oneOrMore, usingZeroOrMore;

  @Setup
  public void setup() {
    int n = Integer.getInteger("n", 3);
    input = Strings.repeat("t", n);
    ParsingExpression subExpression = new StringExpression("t");
    oneOrMore = compile(new OneOrMoreExpression(subExpression));
    usingZeroOrMore = compile(new SequenceExpression(subExpression, new ZeroOrMoreExpression(subExpression)));
  }

  @GenerateMicroBenchmark
  public boolean oneOrMore() {
    return Machine.execute(input, oneOrMore);
  }

  @GenerateMicroBenchmark
  public boolean usingZeroOrMore() {
    return Machine.execute(input, usingZeroOrMore);
  }

  private Instruction[] compile(ParsingExpression expression) {
    return new SequenceExpression(expression, EndOfInputExpression.INSTANCE).compile(new CompilationHandler());
  }

}