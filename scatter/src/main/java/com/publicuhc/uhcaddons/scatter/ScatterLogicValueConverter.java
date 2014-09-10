/*
 * ScatterLogicValueConverter.java
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

package com.publicuhc.uhcaddons.scatter;

import com.publicuhc.scatterlib.logic.RandomCircleScatterLogic;
import com.publicuhc.scatterlib.logic.RandomSquareScatterLogic;
import com.publicuhc.scatterlib.logic.StandardScatterLogic;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.ValueConversionException;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.ValueConverter;

import java.util.Random;

public class ScatterLogicValueConverter implements ValueConverter<StandardScatterLogic> {
    @Override
    public StandardScatterLogic convert(String value)
    {
        if (value.equalsIgnoreCase("circle"))
            return new RandomCircleScatterLogic(new Random());
        if (value.equalsIgnoreCase("square"))
            return new RandomSquareScatterLogic(new Random());
        throw new ValueConversionException("Invalid scatter type");
    }

    @Override
    public Class<StandardScatterLogic> valueType()
    {
        return StandardScatterLogic.class;
    }

    @Override
    public String valuePattern()
    {
        return null;
    }
}
