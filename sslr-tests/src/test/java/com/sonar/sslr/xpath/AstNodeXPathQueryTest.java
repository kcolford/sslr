/*
 * Copyright (C) 2010 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */
package com.sonar.sslr.xpath;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.xpath.api.AstNodeXPathQuery;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class AstNodeXPathQueryTest {

  @Test
  public void selectSingleNodeTest() {
    AstNodeXPathQuery<AstNode> expr = AstNodeXPathQuery.create("branch/leaf");
    AstNode tree = new AstNode(new NodeType(), "tree", null);
    AstNode branch = new AstNode(new NodeType(), "branch", null);
    AstNode leaf = new AstNode(new NodeType(), "leaf", null);
    tree.addChild(branch);
    branch.addChild(leaf);

    assertThat(expr.selectSingleNode(tree), is(leaf));
  }

  @Test
  public void selectSingleNodeNoResultTest() {
    AstNodeXPathQuery<AstNode> expr = AstNodeXPathQuery.create("branch");
    AstNode tree = new AstNode(new NodeType(), "tree", null);

    assertThat(expr.selectSingleNode(tree), nullValue());
  }

  @Test
  public void selectNodesTest() {
    AstNodeXPathQuery<AstNode> expr = AstNodeXPathQuery.create("//leaf");
    AstNode tree = new AstNode(new NodeType(), "tree", null);
    AstNode branch = new AstNode(new NodeType(), "branch", null);
    AstNode leaf1 = new AstNode(new NodeType(), "leaf", null);
    AstNode leaf2 = new AstNode(new NodeType(), "leaf", null);
    tree.addChild(branch);
    branch.addChild(leaf1);
    branch.addChild(leaf2);

    assertThat(expr.selectNodes(tree).size(), is(2));
  }

  @Test
  public void selectNodesNoResultTest() {
    AstNodeXPathQuery<AstNode> expr = AstNodeXPathQuery.create("//branch");
    AstNode tree = new AstNode(new NodeType(), "tree", null);

    assertThat(expr.selectNodes(tree).size(), is(0));
  }

  @Test
  public void relativePathTest() {
    AstNodeXPathQuery<AstNode> expr = AstNodeXPathQuery.create("leaf");
    AstNode tree = new AstNode(new NodeType(), "tree", null);
    AstNode branch = new AstNode(new NodeType(), "branch", null);
    AstNode leaf = new AstNode(new NodeType(), "leaf", null);

    tree.addChild(branch);
    branch.addChild(leaf);

    assertThat(expr.selectSingleNode(branch), is(leaf));
  }

  @Test
  public void parentPathTest() {
    AstNodeXPathQuery<AstNode> expr = AstNodeXPathQuery.create("..");
    AstNode tree = new AstNode(new NodeType(), "tree", null);
    AstNode branch = new AstNode(new NodeType(), "branch", null);
    AstNode leaf = new AstNode(new NodeType(), "leaf", null);

    tree.addChild(branch);
    branch.addChild(leaf);

    assertThat(expr.selectSingleNode(branch), is(tree));
  }

  @Test
  public void parentAndDescendingPathTest() {
    AstNodeXPathQuery<AstNode> expr = AstNodeXPathQuery.create("../branch2");
    AstNode tree = new AstNode(new NodeType(), "tree", null);

    AstNode branch1 = new AstNode(new NodeType(), "branch1", null);
    AstNode leaf = new AstNode(new NodeType(), "leaf", null);

    AstNode branch2 = new AstNode(new NodeType(), "branch2", null);

    tree.addChild(branch1);
    tree.addChild(branch2);

    branch1.addChild(leaf);

    assertThat(expr.selectSingleNode(branch1), is(branch2));
  }

  @Test
  public void absolutePathTest() {
    AstNodeXPathQuery<AstNode> expr = AstNodeXPathQuery.create("/tree");
    AstNode tree = new AstNode(new NodeType(), "tree", null);
    AstNode branch = new AstNode(new NodeType(), "branch", null);
    AstNode leaf = new AstNode(new NodeType(), "leaf", null);

    tree.addChild(branch);
    branch.addChild(leaf);

    assertThat(expr.selectSingleNode(tree), is(tree));
  }

  @Test
  public void currentPathTest() {
    AstNodeXPathQuery<AstNode> expr = AstNodeXPathQuery.create(".");
    AstNode tree = new AstNode(new NodeType(), "tree", null);
    AstNode branch = new AstNode(new NodeType(), "branch", null);
    AstNode leaf = new AstNode(new NodeType(), "leaf", null);

    tree.addChild(branch);
    branch.addChild(leaf);

    assertThat(expr.selectSingleNode(branch), is(branch));
  }

  @Test
  public void currentPathWithDescendantTest() {
    AstNodeXPathQuery<AstNode> expr = AstNodeXPathQuery.create("./leaf");
    AstNode tree = new AstNode(new NodeType(), "tree", null);
    AstNode branch = new AstNode(new NodeType(), "branch", null);
    AstNode leaf = new AstNode(new NodeType(), "leaf", null);

    tree.addChild(branch);
    branch.addChild(leaf);

    assertThat(expr.selectSingleNode(branch), is(leaf));
  }

  @Test
  public void singleDocumentRoot() {
    AstNodeXPathQuery<AstNode> expr = AstNodeXPathQuery.create("//tree");
    AstNode tree = new AstNode(new NodeType(), "tree", null);
    AstNode branch = new AstNode(new NodeType(), "branch", null);
    AstNode leaf = new AstNode(new NodeType(), "leaf", null);

    tree.addChild(branch);
    branch.addChild(leaf);

    assertThat(expr.selectNodes(tree).size(), is(1));
  }

  @Test
  public void relativeNamePredicate() {
    AstNodeXPathQuery<AstNode> expr = AstNodeXPathQuery.create(".[name() = \"tree\"]");
    AstNode tree = new AstNode(new NodeType(), "tree", null);

    assertThat(expr.selectSingleNode(tree), is(tree));
  }

  @Test
  public void relativeCountPredicate() {
    AstNodeXPathQuery<AstNode> expr = AstNodeXPathQuery.create(".[count(*) = 3]");
    AstNode tree = new AstNode(new NodeType(), "tree", null);

    AstNode branch1 = new AstNode(new NodeType(), "branch1", null);
    AstNode branch2 = new AstNode(new NodeType(), "branch2", null);
    AstNode branch3 = new AstNode(new NodeType(), "branch3", null);

    tree.addChild(branch1);
    tree.addChild(branch2);
    tree.addChild(branch3);

    assertThat(expr.selectSingleNode(tree), is(tree));
  }

  @Test
  public void noCacheTest() {
    AstNodeXPathQuery<AstNode> expr = AstNodeXPathQuery.create("//branch");

    AstNode tree1 = new AstNode(new NodeType(), "tree", null);
    AstNode branch11 = new AstNode(new NodeType(), "branch", null);
    AstNode branch12 = new AstNode(new NodeType(), "branch", null);
    AstNode branch13 = new AstNode(new NodeType(), "branch", null);
    tree1.addChild(branch11);
    tree1.addChild(branch12);
    tree1.addChild(branch13);

    assertThat(expr.selectNodes(tree1).size(), is(3));

    AstNode tree2 = new AstNode(new NodeType(), "tree", null);
    AstNode branch21 = new AstNode(new NodeType(), "branch", null);
    tree2.addChild(branch21);

    assertThat(expr.selectNodes(tree2).size(), is(1));
  }

  static class NodeType implements AstNodeType {

  }

}
