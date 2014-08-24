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
