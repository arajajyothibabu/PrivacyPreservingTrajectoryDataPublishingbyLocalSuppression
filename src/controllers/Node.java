package controllers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Araja Jyothi Babu on 18-May-16.
 */
class Node
{
    int noOfMFS = 0;
    char name;
    List<Node> children =  new ArrayList<Node>();

    Node(char name){
        this.noOfMFS++;
        this.name = name;
    }
}
