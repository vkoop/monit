package de.vkoop.monit.filter;

public interface StatefulFilter<T> extends Filter<T>{
    /**
     * Check if the item is new.
     *
     * @param item to check
     * @return true if the item is new.
     */
    boolean isNewItem(T item);

    void blockItem(T item);

    void unblockItem(T item);

    @Override
    default boolean canPass(T item){
        boolean isNewItem = isNewItem(item);

        if(isNewItem){
            blockItem(item);
        }

        return isNewItem;
    }

    /**
     * Restore the item to original state.
     *
     * @param item to restore
     * @return
     * <ul>
     *     <li>
     *         true - if restored the item
     *     </li>
     *     <li>
     *         false - else
     *     </li>
     * </ul>
     */
    default boolean restore(T item) {
        boolean isNewItem = isNewItem(item);

        if(!isNewItem){
            unblockItem(item);
        }

        return !isNewItem;
    }
}
