package Iterator;

import java.util.List;

public class ExtendedIteratorImpl<T> implements ExtendedIterator<T> {

    private List<T> list;
    private int position;

    public ExtendedIteratorImpl(List<T> list) {
        this.list = list;
        this.position = 0; // al inicio
    }

    @Override
    public boolean hasNext() {
        return position < list.size();
    }

    @Override
    public T next() {
        return list.get(position++);
    }


    @Override
    public boolean hasPrevious() {
        return position > 0;
    }

    @Override
    public T previous() {
        position--;
        return list.get(position);
    }

    @Override
    public void goFirst() {
        position = 0;
    }

    @Override
    public void goLast() {
        position = list.size();
    }
}