package com.hewutao.db.model2.support;

import com.hewutao.db.model2.Delay;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

public class DelayList<E, T> implements Delay<List<T>>, List<T> {
    private Function<E, List<T>> supplier;
    private E entity;
    private List<T> original;
    private List<T> current;

    public DelayList(Function<E, List<T>> supplier) {
        this.supplier = supplier;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    @Override
    public List<T> getOriginal() {
        return original;
    }

    @Override
    public boolean loaded() {
        return current != null;
    }

    private void load() {
        if (!loaded()) {
            this.original = supplier.apply(entity);
            this.current = new ArrayList<>(this.original);
            this.supplier = null;
            this.entity = null;
        }
    }

    @Override
    public int size() {
        load();
        return current.size();
    }

    @Override
    public boolean isEmpty() {
        load();
        return current.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        load();
        return current.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        load();
        return current.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        load();
        current.forEach(action);
    }

    @Override
    public Object[] toArray() {
        load();
        return current.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        load();
        return current.toArray(a);
    }

    @Override
    public boolean add(T t) {
        load();
        return current.add(t);
    }

    @Override
    public boolean remove(Object o) {
        load();
        return current.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        load();
        return current.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        load();
        return current.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        load();
        return current.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        load();
        return current.removeAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        load();
        return current.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        load();
        return current.retainAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        load();
        current.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super T> c) {
        load();
        current.sort(c);
    }

    @Override
    public void clear() {
        load();
        current.clear();
    }

    @Override
    public boolean equals(Object o) {
        load();
        return current.equals(o);
    }

    @Override
    public int hashCode() {
        load();
        return current.hashCode();
    }

    @Override
    public T get(int index) {
        load();
        return current.get(index);
    }

    @Override
    public T set(int index, T element) {
        load();
        return current.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        load();
        current.add(index, element);
    }

    @Override
    public T remove(int index) {
        load();
        return current.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        load();
        return current.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        load();
        return current.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        load();
        return current.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        load();
        return current.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        load();
        return current.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<T> spliterator() {
        load();
        return current.spliterator();
    }

    @Override
    public Stream<T> stream() {
        load();
        return current.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        load();
        return current.parallelStream();
    }
}
