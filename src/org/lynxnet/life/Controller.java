package org.lynxnet.life;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.util.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 22/9/2006
 * Time: 23:49:34
 * To change this template use File | Settings | File Templates.
 */
public class Controller {
    private static final String SCHEMA_NAME = "Life.xsd";

    private Surface model;

    public Controller() {
        model = new Surface(null);
    }

    public Surface changeGeneration() {
        Set<Cell> newCells = new TreeSet<Cell>();
        Iterator axesIter =
                model.getAxisIterator();
        while (axesIter.hasNext()) {
            Map.Entry entry = (Map.Entry) axesIter.next();
            SortedMap<Integer, Cell> yAxis = (SortedMap<Integer, Cell>) entry.getValue();
            for (Cell cell : yAxis.values()) {
                for (int x = cell.getX() - 1; x <= cell.getX() + 1; x++) {
                    for (int y = cell.getY() - 1; y <= cell.getY() + 1; y++) {
                        Collection<Cell> neighbors = model.findNeighbors(x, y);
                        if ((neighbors.size() < 2) || (neighbors.size() > 3)) {
                            continue;
                        }
                        Cell cellToSave = model.getCell(x, y);
                        if (cellToSave != null) {
                            cellToSave.increment();
                        } else if (neighbors.size() == 3) {
                            cellToSave = new Cell(x, y);
                        }
                        if (cellToSave != null) {
                            newCells.add(cellToSave);
                        }
                    }
                }
            }
        }
        model = new Surface(newCells);
        return model;
    }

    public Surface getModel() {
        return model;
    }

    public void load(String configFileName) throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Source schemaSource = new StreamSource(Controller.class.getResourceAsStream(SCHEMA_NAME));
        Schema schema = schemaFactory.newSchema(schemaSource);

        factory.setSchema(schema);
        SAXParser parser = factory.newSAXParser();
        parser.parse(new File(configFileName), new LifeHandler());
    }

    public void save() {

    }

    enum HandlerState {
        NONE, X, Y, GENERATION
    }

    class LifeHandler extends DefaultHandler {
        private HandlerState state = HandlerState.NONE;
        private Integer x, y, generation;

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (localName.equals("x")) {
                state = HandlerState.X;
            }
            if (localName.equals("y")) {
                state = HandlerState.Y;
            }
            if (localName.equals("generation")) {
                state = HandlerState.GENERATION;
            }
        }

        public void characters(char ch[], int start, int length) throws SAXException {
            switch (state) {
                case X:
                    x = Integer.parseInt(new String(ch, start, length));
                    break;
                case Y:
                    y = Integer.parseInt(new String(ch, start, length));
                    break;
                case GENERATION:
                    generation = Integer.parseInt(new String(ch, start, length));
                    break;
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            state = HandlerState.NONE;
            if (localName.equals("cell")) {
                Cell cell = new Cell(x, y);
                if (generation != null) {
                    cell.setAge(generation);
                }
                Controller.this.getModel().addCell(cell);
                x = null;
                y = null;
                generation = null;
            }
        }
    }
}
