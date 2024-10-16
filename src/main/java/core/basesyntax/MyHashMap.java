package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfFull();
        int index = calculateIndex(table.length, hash(key));
        Node<K, V> newNode = new Node<>(key, value);

        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    break;
                }
                current = current.next;
            }
        }

        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return (node == null) ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private int calculateIndex(int length, int hash) {
        return (length - 1) & hash;
    }

    private Node<K, V> getNode(K key) {
        int index = calculateIndex(table.length, hash(key));
        Node<K, V> node = table[index];

        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void resizeIfFull() {
        if (size == table.length * LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = table.length * GROW_FACTOR;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                int newHash = calculateIndex(newCapacity, hash(node.key));
                Node<K, V> next = node.next;
                node.next = newTable[newHash];
                newTable[newHash] = node;
                node = next;
            }
        }
        table = newTable;

    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
