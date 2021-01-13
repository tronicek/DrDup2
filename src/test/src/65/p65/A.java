package p65;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class A {

    Element getFirstChildElement(Node parent, String elemName) {
        Node child = parent.getFirstChild();
        while (child != null) {
            child = child.getNextSibling();
        }
        return null;
    }

    Element getNextSiblingElement(Node node, String elemName) {
        Node sibling = node.getNextSibling();
        while (sibling != null) {
            sibling = sibling.getNextSibling();
        }
        return null;
    }
}
