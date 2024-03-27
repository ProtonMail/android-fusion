/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 by Proton Technologies A.G. (Switzerland) Email: contact@protonmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package me.proton.test.fusion.ui.compose.wrappers

import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.onAncestors
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.onSibling
import androidx.compose.ui.test.onSiblings
import me.proton.test.fusion.ui.compose.builders.OnNode
import me.proton.test.fusion.ui.compose.builders.OnNodes

interface NodeFinders : NodeAssertions {
    /** Single node filters **/
    /** returns node at child [position] **/
    fun onChildAt(position: Int): NodeActions = OnNode(interaction.onChildAt(position))

    /** returns a child node (only if node has one child) **/
    fun onChild(): NodeActions = OnNode(interaction.onChild())

    /** returns a parent node (only if node has one parent) **/
    fun onParent(): NodeActions = OnNode(interaction.onParent())

    /** returns a sibling node (only if node has one sibling) **/
    fun onSibling(): NodeActions = OnNode(interaction.onSibling())

    /** returns a node from children with matcher from given [node] **/
    fun onChild(node: OnNode): NodeActions = OnNode(interaction.onChildren().filterToOne(node.matcher))

    /** returns a node from children with matcher from given [ancestor] **/
    fun onAncestor(ancestor: OnNode): NodeActions = OnNode(interaction.onAncestors().filterToOne(ancestor.matcher))

    /** returns a node from siblings with matcher from given [sibling] **/
    fun onSibling(sibling: OnNode): NodeActions = OnNode(interaction.onSiblings().filterToOne(sibling.matcher))

    /** Node collection filters **/
    /** returns children node collection **/
    fun onChildren(): NodeCollectionActions = OnNodes(interaction.onChildren())

    /** returns sibling node collection **/
    fun onSiblings(): NodeCollectionActions = OnNodes(interaction.onSiblings())

    /** returns ancestor node collection **/
    fun onAncestors(): NodeCollectionActions = OnNodes(interaction.onAncestors())
}
