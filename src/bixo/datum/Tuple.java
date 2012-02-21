/*
 * Copyright (c) 2007-2010 Concurrent, Inc. All Rights Reserved.
 *
 * Project and contact information: http://www.cascading.org/
 *
 * This file is part of the Cascading project.
 *
 * Cascading is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cascading is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cascading.  If not, see <http://www.gnu.org/licenses/>.
 */

package bixo.datum;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * A Tuple represents a set of values. Consider a Tuple the same as a data base
 * record where every value is a column in that table. A Tuple stream would be a
 * set of Tuple instances, which are passed consecutively through a Pipe
 * assembly.
 * <p/>
 * A Tuple is a collection of elements. These elements must be of type
 * Comparable, so that Tuple instances can be compared. Tuple itself is
 * Comparable and subsequently can hold elements of type Tuple.
 * <p/>
 * Tuples are mutable for sake of efficiency. Since Tuples are mutable, it is
 * not a good idea to hold an instance around with out first copying it via its
 * copy constructor, a subsequent {@link Pipe} could change the Tuple in place.
 * This is especially true for {@link Aggregator} operators.
 * <p/>
 * Because a Tuple can hold any Comparable type, it is suitable for storing
 * custom types. But all custom types must have a serialization support per the
 * underlying framework.
 * <p/>
 * For Hadoop, a {@link org.apache.hadoop.io.serializer.Serialization}
 * implementation must be registered with Hadoop. For further performance
 * improvements, see the {@link cascading.tuple.hadoop.SerializationToken} Java
 * annotation.
 * 
 * @see Comparable
 * @see org.apache.hadoop.io.serializer.Serialization
 * @see cascading.tuple.hadoop.SerializationToken
 */
@SuppressWarnings("rawtypes")
public class Tuple implements Comparable, Iterable, Serializable {

  private static final long serialVersionUID = 1L;

  /** Field isUnmodifiable */
  protected boolean isUnmodifiable = false;
  /** Field elements */
  protected List<Object> elements;

  /**
   * Method asUnmodifiable marks the given Tuple instance as unmodifiable.
   * 
   * @param tuple
   *          of type Tuple
   * @return Tuple
   * @deprecated
   */
  @Deprecated
  public static Tuple asUnmodifiable(Tuple tuple) {
    tuple.isUnmodifiable = true;

    return tuple;
  }

  /**
   * Method size returns a new Tuple instance of the given size with nulls as
   * its element values.
   * 
   * @param size
   *          of type int
   * @return Tuple
   */
  public static Tuple size(int size) {
    return size(size, null);
  }

  /**
   * Method size returns a new Tuple instance of the given size with the given
   * Comparable as its element values.
   * 
   * @param size
   *          of type int
   * @param value
   *          of type Comparable
   * @return Tuple
   */
  public static Tuple size(int size, Comparable value) {
    Tuple result = new Tuple();

    for (int i = 0; i < size; i++)
      result.add(value);

    return result;
  }

  /**
   * Method parse will parse the {@link #print()} String representation of a
   * Tuple instance and return a new Tuple instance.
   * <p/>
   * This method has been deprecated as it doesn't properly handle nulls, and
   * any types other than primitive types.
   * 
   * @param string
   *          of type String
   * @return Tuple
   * @deprecated
   */
  @Deprecated
  public static Tuple parse(String string) {
    if (string == null || string.length() == 0)
      return null;

    string = string.replaceAll("^ *\\[*", "");
    string = string.replaceAll("\\]* *$", "");

    Scanner scanner = new Scanner(new StringReader(string));
    scanner.useDelimiter("(' *, *')|(^ *')|(' *$)");

    Tuple result = new Tuple();

    while (scanner.hasNext()) {
      if (scanner.hasNextInt())
        result.add(scanner.nextInt());
      else if (scanner.hasNextDouble())
        result.add(scanner.nextDouble());
      else
        result.add(scanner.next());
    }

    scanner.close();

    return result;
  }

  /**
   * Returns a reference to the private elements of the given Tuple.
   * <p/>
   * This method is for internal use and is subject to change scope in a future
   * release.
   * 
   * @param tuple
   *          of type Tuple
   * @return List<Comparable>
   */
  public static List<Object> elements(Tuple tuple) {
    return tuple.elements;
  }

  protected Tuple(List<Object> elements) {
    this.elements = elements;
  }

  /** Constructor Tuple creates a new Tuple instance. */
  public Tuple() {
    this(new ArrayList<Object>());
  }

  /**
   * Copy constructor. Does not nest the given Tuple instance within this new
   * instance. Use {@link #add(Object)}.
   * 
   * @param tuple
   *          of type Tuple
   */
  @ConstructorProperties({"tuple"})
  public Tuple(Tuple tuple) {
    this();
    elements.addAll(tuple.elements);
  }

  /**
   * Constructor Tuple creates a new Tuple instance with all the given values.
   * 
   * @param values
   *          of type Object...
   */
  @ConstructorProperties({"values"})
  public Tuple(Object... values) {
    this();
    Collections.addAll(elements, values);
  }

  /**
   * Method isUnmodifiable returns true if this Tuple instance is unmodifiable.
   * 
   * @return boolean
   */
  public boolean isUnmodifiable() {
    return isUnmodifiable;
  }

  /**
   * Method get returns the element at the given position i.
   * <p/>
   * This method assumes the element implements {@link Comparable} in order to
   * maintain backwards compatibility. See {@link #getObject(int)} for an
   * alternative.
   * 
   * @param pos
   *          of type int
   * @return Comparable
   */
  public Comparable get(int pos) {
    return (Comparable) elements.get(pos);
  }

  /**
   * Method get returns the element at the given position i.
   * 
   * @param pos
   *          of type int
   * @return Comparable
   */
  public Object getObject(int pos) {
    return elements.get(pos);
  }

  /**
   * Method getString returns the element at the given position i as a String.
   * 
   * @param pos
   *          of type int
   * @return String
   */
  public String getString(int pos) {
    return Tuples.toString(getObject(pos));
  }

  /**
   * Method getFloat returns the element at the given position i as a float.
   * Zero if null.
   * 
   * @param pos
   *          of type int
   * @return float
   */
  public float getFloat(int pos) {
    return Tuples.toFloat(getObject(pos));
  }

  /**
   * Method getDouble returns the element at the given position i as a double.
   * Zero if null.
   * 
   * @param pos
   *          of type int
   * @return double
   */
  public double getDouble(int pos) {
    return Tuples.toDouble(getObject(pos));
  }

  /**
   * Method getInteger returns the element at the given position i as an int.
   * Zero if null.
   * 
   * @param pos
   *          of type int
   * @return int
   */
  public int getInteger(int pos) {
    return Tuples.toInteger(getObject(pos));
  }

  /**
   * Method getLong returns the element at the given position i as an long. Zero
   * if null.
   * 
   * @param pos
   *          of type int
   * @return long
   */
  public long getLong(int pos) {
    return Tuples.toLong(getObject(pos));
  }

  /**
   * Method getShort returns the element at the given position i as an short.
   * Zero if null.
   * 
   * @param pos
   *          of type int
   * @return long
   */
  public short getShort(int pos) {
    return Tuples.toShort(getObject(pos));
  }

  /**
   * Method getBoolean returns the element at the given position as a boolean.
   * If the value is (case ignored) the string 'true', a {@code true} value will
   * be returned. {@code false} if null.
   * 
   * @param pos
   *          of type int
   * @return boolean
   */
  public boolean getBoolean(int pos) {
    return Tuples.toBoolean(getObject(pos));
  }

  /**
   * Method get will return a new Tuple instace populated with element values
   * from the given array of positions.
   * 
   * @param pos
   *          of type int[]
   * @return Tuple
   */
  public Tuple get(int[] pos) {
    if (pos == null || pos.length == 0)
      return new Tuple(this);

    Tuple results = new Tuple();

    for (int i : pos)
      results.add(elements.get(i));

    return results;
  }

  /**
   * Method is the inverse of {@link #remove(int[])}.
   * 
   * @param pos
   *          of type int[]
   * @return Tuple
   */
  public Tuple leave(int[] pos) {
    verifyModifiable();

    Tuple results = remove(pos);

    List<Object> temp = results.elements;
    results.elements = this.elements;
    this.elements = temp;

    return results;
  }

  /**
   * Method clear empties this Tuple instance. A subsequent call to
   * {@link #size()} will return zero ({@code 0}).
   */
  public void clear() {
    verifyModifiable();

    elements.clear();
  }

  /**
   * Method add adds a new element value to this instance.
   * 
   * @param value
   *          of type Comparable
   */
  public void add(Comparable value) {
    add((Object) value);
  }

  /**
   * Method add adds a new element value to this instance.
   * 
   * @param value
   *          of type Object
   */
  public void add(Object value) {
    verifyModifiable();

    elements.add(value);
  }

  /**
   * Method addAll adds all given values to this instance.
   * 
   * @param values
   *          of type Object...
   */
  public void addAll(Object... values) {
    verifyModifiable();

    if (values.length == 1 && values[0] instanceof Tuple)
      addAll((Tuple) values[0]);
    else
      Collections.addAll(elements, values);
  }

  /**
   * Method addAll adds all the element values of the given Tuple instance to
   * this instance.
   * 
   * @param tuple
   *          of type Tuple
   */
  public void addAll(Tuple tuple) {
    verifyModifiable();

    if (tuple != null)
      elements.addAll(tuple.elements);
  }

  /**
   * Method set sets the given value to the given index position in this
   * instance.
   * 
   * @param index
   *          of type int
   * @param value
   *          of type Object
   */
  public void set(int index, Object value) {
    verifyModifiable();

    try {
      elements.set(index, value);
    } catch (IndexOutOfBoundsException exception) {
      if (elements.size() != 0)
        throw new RuntimeException("failed to set a value beyond the end of the tuple elements array, size: " + size() + " , index: " + index);
      else
        throw new RuntimeException("failed to set a value, tuple may not be initialized with values, is zero length");
    }
  }

  /**
   * Method remove removes the values specified by the given pos array and
   * returns a new Tuple containing the removed values.
   * 
   * @param pos
   *          of type int[]
   * @return Tuple
   */
  public Tuple remove(int[] pos) {
    verifyModifiable();

    // calculate offsets to apply when removing values from elements
    int offset[] = new int[pos.length];

    for (int i = 0; i < pos.length; i++) {
      offset[i] = 0;

      for (int j = 0; j < i; j++) {
        if (pos[j] < pos[i])
          offset[i]++;
      }
    }

    Tuple results = new Tuple();

    for (int i = 0; i < pos.length; i++)
      results.add(elements.remove(pos[i] - offset[i]));

    return results;
  }

  /**
   * Creates a new Tuple from the given positions, but sets the values in the
   * current tuple to null.
   * 
   * @param pos
   *          of type int[]
   * @return Tuple
   */
  Tuple extract(int[] pos) {
    Tuple results = new Tuple();

    for (int i : pos)
      results.add(elements.set(i, null));

    return results;
  }

  /**
   * Sets the values in the given positions to the values from the given Tuple.
   * 
   * @param pos
   *          of type int[]
   * @param tuple
   *          of type Tuple
   */
  void set(int[] pos, Tuple tuple) {
    verifyModifiable();

    if (pos.length != tuple.size())
      throw new RuntimeException("given tuple not same size as position array, tuple: " + tuple.print());

    int count = 0;
    for (int i : pos)
      elements.set(i, tuple.elements.get(count++));
  }

  /**
   * Method iterator returns an {@link Iterator} over this Tuple instances
   * values.
   * 
   * @return Iterator
   */
  public Iterator iterator() {
    return elements.iterator();
  }

  /**
   * Method isEmpty returns true if this Tuple instance has no values.
   * 
   * @return the empty (type boolean) of this Tuple object.
   */
  public boolean isEmpty() {
    return elements.isEmpty();
  }

  /**
   * Method size returns the number of values in this Tuple instance.
   * 
   * @return int
   */
  public int size() {
    return elements.size();
  }

  /**
   * Method elements returns a new Object[] array of this Tuple instances
   * values.
   * 
   * @return Object[]
   */
  private Object[] elements() {
    return elements.toArray();
  }

  /**
   * Method elements returns the given destination array with the values of This
   * tuple instance.
   * 
   * @param destination
   *          of type Object[]
   * @return Object[]
   */
  Object[] elements(Object[] destination) {
    return elements.toArray(destination);
  }

  /**
   * Method getTypes returns an array of the element classes. Null if the
   * element is null.
   * 
   * @return the types (type Class[]) of this Tuple object.
   */
  public Class[] getTypes() {
    Class[] types = new Class[elements.size()];

    for (int i = 0; i < elements.size(); i++) {
      Object value = elements.get(i);

      if (value != null)
        types[i] = value.getClass();
    }

    return types;
  }

  /**
   * Method append appends all the values of the given Tuple instances to a copy
   * of this instance.
   * 
   * @param tuples
   *          of type Tuple
   * @return Tuple
   */
  public Tuple append(Tuple... tuples) {
    Tuple result = new Tuple(this);

    for (Tuple tuple : tuples)
      result.addAll(tuple);

    return result;
  }

  /**
   * Method compareTo compares this Tuple to the given Tuple instance.
   * 
   * @param other
   *          of type Tuple
   * @return int
   */
  @SuppressWarnings("unchecked")
  public int compareTo(Tuple other) {
    if (other == null || other.elements == null)
      return 1;

    if (other.elements.size() != this.elements.size())
      return this.elements.size() - other.elements.size();

    for (int i = 0; i < this.elements.size(); i++) {
      Comparable lhs = (Comparable) this.elements.get(i);
      Comparable rhs = (Comparable) other.elements.get(i);

      if (lhs == null && rhs == null)
        continue;

      if (lhs == null && rhs != null)
        return -1;
      else if (lhs != null && rhs == null)
        return 1;

      int c = lhs.compareTo(rhs); // guaranteed to not be null
      if (c != 0)
        return c;
    }

    return 0;
  }

  @SuppressWarnings("unchecked")
  public int compareTo(Comparator[] comparators, Tuple other) {
    if (comparators == null)
      return compareTo(other);

    if (other == null || other.elements == null)
      return 1;

    if (other.elements.size() != this.elements.size())
      return this.elements.size() - other.elements.size();

    if (comparators.length != this.elements.size())
      throw new IllegalArgumentException("comparator array not same size as tuple elements");

    for (int i = 0; i < this.elements.size(); i++) {
      Object lhs = this.elements.get(i);
      Object rhs = other.elements.get(i);

      int c = 0;

      if (comparators[i] != null)
        c = comparators[i].compare(lhs, rhs);
      else if (lhs == null && rhs == null)
        c = 0;
      else if (lhs == null && rhs != null)
        return -1;
      else if (lhs != null && rhs == null)
        return 1;
      else
        c = ((Comparable) lhs).compareTo((Comparable) rhs); // guaranteed to not
                                                            // be null

      if (c != 0)
        return c;
    }

    return 0;
  }

  /**
   * Method compareTo implements the {@link Comparable#compareTo(Object)}
   * method.
   * 
   * @param other
   *          of type Object
   * @return int
   */
  public int compareTo(Object other) {
    if (other instanceof Tuple)
      return compareTo((Tuple) other);
    else
      return -1;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Tuple))
      return false;

    Tuple other = (Tuple) object;

    if (this.elements.size() != other.elements.size())
      return false;

    for (int i = 0; i < this.elements.size(); i++) {
      Object lhs = this.elements.get(i);
      Object rhs = other.elements.get(i);

      if (lhs == null && rhs == null)
        continue;

      if (lhs == null || rhs == null)
        return false;

      if (!lhs.equals(rhs))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hash = 1;

    for (Object element : elements)
      hash = 31 * hash + (element != null ? element.hashCode() : 0);

    return hash;
  }

  /**
   * Method format uses the {@link Formatter} class for formatting this tuples
   * values into a new string.
   * 
   * @param format
   *          of type String
   * @return String
   */
  public String format(String format) {
    return String.format(format, elements());
  }

  /**
   * Method print returns a parsable String representation of this Tuple
   * instance.
   * 
   * @return String
   */
  public String print() {
    return print(new StringBuffer()).toString();
  }

  protected StringBuffer print(StringBuffer buffer) {
    buffer.append("[");

    if (elements != null) {
      for (int i = 0; i < elements.size(); i++) {
        Object element = elements.get(i);

        if (element instanceof Tuple)
          ((Tuple) element).print(buffer);
        else if (element == null) // don't quote nulls to distinguish from null
                                  // strings
          buffer.append(element);
        else
          buffer.append("\'").append(element).append("\'");

        if (i < elements.size() - 1)
          buffer.append(", ");
      }
    }

    buffer.append("]");

    return buffer;
  }

  private final void verifyModifiable() {
    if (isUnmodifiable)
      throw new UnsupportedOperationException("this tuple is unmodifiable");
  }

}