package com.taboola.tests.ex2;

/**
 * Created by michael on 6/12/18.
 */

import java.util.Date;
import java.util.List;

public class MyClass {
    private Date m_time;
    private String m_name;
    private List<Long> m_numbers;
    private List<String> m_strings;

    public MyClass(Date time, String name, List<Long> numbers, List<String> strings) {
        m_time = time;
        m_name = name;
        // We assign here a reference to a List ... if the list is modified outside this class, it will affect this MyClass as well !
        // We can clone those list here , if it's feasible from a product point of view ...
        m_numbers = numbers;
        m_strings = strings;


    }

    /**
     * Fix: do not compare only the m_name but all the data members !
     *
     * @param obj
     * @return
     */
    public boolean equals_BUGGY(Object obj) {
        if (obj instanceof MyClass) {
            return m_name.equals(((MyClass) obj).m_name);
        }
        return false;
    }

    /**
     * Here, we check each data member and not only the m_name(including the list ) . <br/>
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyClass myClass = (MyClass) o;

        if (!m_time.equals(myClass.m_time)) return false;
        if (!m_name.equals(myClass.m_name)) return false;
        if (!m_numbers.equals(myClass.m_numbers)) return false;
        return m_strings.equals(myClass.m_strings);
    }

    /**
     * In addition to the equals, we also implement the hashCode of course. <br/>
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = m_time.hashCode();
        result = 31 * result + m_name.hashCode();
        result = 31 * result + m_numbers.hashCode();
        result = 31 * result + m_strings.hashCode();
        return result;
    }

    /**
     * Return textual representation of ALL the data members <br/>
     * Remember that this code is not thread safe, as we iterate over list (m_numbers / m_strings) that might be modified in the middle of our loop <br/>
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("name = ").append(m_name);
        out.append("date = ").append(m_time);

        for (int i = 0; i < m_numbers.size(); i++) {
            out.append("m_numbers[").append(i).append("]=").append(m_numbers.get(i));
        }
        for (int i = 0; i < m_strings.size(); i++) {
            out.append("m_strings[").append(i).append("]=").append(m_strings.get(i));
        }

        return out.toString();
    }


    /**
     * RECURSIVE implementation, as we can't remove a member of a list in a middle of iteration (well, when iteration is made with this kind of for-lopp ...)
     *
     * @param str
     */
    public void removeString(String str) {
        for (int i = 0; i < m_strings.size(); i++) {
            if (m_strings.get(i).equals(str)) {
                m_strings.remove(i);
                removeString(str);
            }
        }
    }

    /**
     * As for other method, this method is not thread safe. <br/>
     *
     * @param number
     * @return
     */
    public boolean containsNumber(long number) {
        for (long num : m_numbers) {
            if (num == number) {
                return true;
            }
        }
        return false;
    }


    /**
     * We might want to introduce TIME ZONE in this method, as it is only implied ...
     * But I'm not sure about it.
     * @return
     */
    public boolean isHistoric() {
        return m_time.before(new Date());
    }
}