package com.landscapesreimagined.forgefrontier.mixinInterfaces;

import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;

import java.util.function.Consumer;

public interface ListenToRecipeFinish {

    /**
     * execute something when a BasinOperatingBlockEntity finishes it's recipe
     * @param entityConsumer Consumer to execute
     * @return an ID used to stop listening to recipe updates
     */
    int subscribeToRecipeFinish(Consumer<BasinOperatingBlockEntity> entityConsumer);

    /**
     * stop listening to these updates
     * @param id
     */
    void unsubscribeToRecipeFinish(int id);
}
