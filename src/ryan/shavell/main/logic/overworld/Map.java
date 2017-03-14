package ryan.shavell.main.logic.overworld;

import org.json.JSONArray;
import org.json.JSONObject;
import ryan.shavell.main.core.Main;
import ryan.shavell.main.render.Drawable;
import ryan.shavell.main.resources.ImageLoader;
import ryan.shavell.main.resources.SpriteSheet;
import ryan.shavell.main.stuff.Log;
import ryan.shavell.main.stuff.Utils;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map implements Drawable {

    private List<SpriteSheet> tilesets = new ArrayList<>();
    private HashMap<Point, Integer> tiles = new HashMap<>();
    private List<Rectangle2D.Double> collisions = new ArrayList<>();

    public Map(String mapJson) {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(SpriteSheet.class.getResource("maps/" + mapJson + ".json").openStream()));
            String json = "";
            String s;
            while ((s=r.readLine()) != null) {
                json += s;
            }
            JSONObject map = new JSONObject(json);
            //List<SpriteSheet> tilesets = new ArrayList<>();
            for (JSONObject t : Utils.getObjects(map.getJSONArray("tilesets"))) {
                int columns = t.getInt("columns");
                int rows = t.getInt("tilecount") / columns;
                int tileWidth = t.getInt("tilewidth");
                int tileHeight = t.getInt("tileheight");
                int spacing = t.getInt("spacing");
                //spacing--;
                //columns++;
                //rows++;
                Log.d("Spacing: " + spacing + ", columns: " + columns + ", rows: " + rows);

                String image = t.getString("image");
                image = image.replace("\\/", "/");
                image = image.replaceFirst("..", "");
                image = image.replace(".png", "");
                String[] parts = image.split("/");
                image = parts[parts.length - 1];

                //Log.d("Image: " + image);

                SpriteSheet sheet = new SpriteSheet(tileWidth, tileHeight, columns, rows, spacing, 0, 0, ImageLoader.getImage(image));
                tilesets.add(sheet);
            }
            for (JSONObject l : Utils.getObjects(map.getJSONArray("layers"))) {
                String type = l.getString("type");
                if (type.equalsIgnoreCase("tilelayer")) {
                    int width = l.getInt("width");
                    int height = l.getInt("height");
                    Integer data;
                    JSONArray dataArray = l.getJSONArray("data");
                    for (int i=0; i<dataArray.length(); i++) {
                        data = (Integer) dataArray.get(i);
                        int index = i;
                        int y = 0;
                        while (index >= width) {
                            index -= width;
                            y += height;
                        }
                        int x = index * width;
                        tiles.put(new Point(x, y), data);
                    }
                } else {
                    for (JSONObject rect : Utils.getObjects(l.getJSONArray("objects"))) {
                        Rectangle2D.Double rectangle = new Rectangle2D.Double(rect.getDouble("x"), rect.getDouble("y"),
                                rect.getDouble("width"), rect.getDouble("height"));
                        collisions.add(rectangle);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick() {}

    @Override
    public void draw(Graphics2D g) {
        //Log.d("told to draw");
        for (Point p : tiles.keySet()) {
            int data = tiles.get(p);
            if (data != 0) {
                SpriteSheet sheet = null;
                for (SpriteSheet s : tilesets) {
                    int size = s.getAllImages().size();
                    if (size < data) {
                        data -= size;
                    } else {
                        sheet = s;
                        break;
                    }
                }
                if (sheet != null) {
                    g.drawImage(sheet.getAllImages().get(data-1), p.x, p.y, null);
                    //Log.d("draw");
                }
            }
        }

        /*
        for (Rectangle2D.Double r : collisions) {
            g.setColor(Color.RED);
            g.draw(r);
        }
        */
    }

    public List<Rectangle2D.Double> getCollisions() {
        return new ArrayList<>(collisions);
    }
}
