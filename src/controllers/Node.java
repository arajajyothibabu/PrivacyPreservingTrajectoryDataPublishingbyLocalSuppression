package controllers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Araja Jyothi Babu on 18-May-16.
 */
class Node
{
    int supportCount = 0;
    char name;
    Node(char name)
    {
        this.supportCount++;
        this.name = name;
    }
    List<Node> children =  new ArrayList<Node>();
}
