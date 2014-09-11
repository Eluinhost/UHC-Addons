/*
 * RecipeChecker.java
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Graham Howden <graham_howden1 at yahoo.co.uk>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.publicuhc.uhcaddons.playerheads;

import org.bukkit.inventory.Recipe;

public interface RecipeChecker
{
    /**
     * Checks if both recipes are equal.<br>
     * Compares both ingredients and results.<br>
     * <br>
     * NOTE: If both arguments are null it returns true.
     *
     * @param recipe1
     * @param recipe2
     * @return true if ingredients and results match, false otherwise.
     * @throws IllegalArgumentException if recipe is other than ShapedRecipe, ShapelessRecipe or FurnaceRecipe.
     */
    boolean areEqual(Recipe recipe1, Recipe recipe2);

    /**
     * Checks if recipes are similar.<br>
     * Only checks ingredients, not results.<br>
     * <br>
     * NOTE: If both arguments are null it returns true. <br>
     *
     * @param recipe1
     * @param recipe2
     * @return true if ingredients match, false otherwise.
     * @throws IllegalArgumentException if recipe is other than ShapedRecipe, ShapelessRecipe or FurnaceRecipe.
     */
    boolean areSimilar(Recipe recipe1, Recipe recipe2);
}
