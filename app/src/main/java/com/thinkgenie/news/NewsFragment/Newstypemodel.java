package com.thinkgenie.news.NewsFragment;

public class Newstypemodel {
    public Newstypemodel() {

    }

    private boolean isSelected = false;
    public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }
        String id;
        String name;

        public Newstypemodel(String id, String name) {
            this.id = id;
            this.name = name;
        }



}
