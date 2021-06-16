package com.threedev.machinetest.home.model;

import java.util.ArrayList;

public class HomeModel {
    private String per_page;

    private String total;

    private ArrayList<Data> data;

    private String page;

    private String total_pages;

    private Support support;

    public String getPer_page ()
    {
        return per_page;
    }

    public void setPer_page (String per_page)
    {
        this.per_page = per_page;
    }

    public String getTotal ()
    {
        return total;
    }

    public void setTotal (String total)
    {
        this.total = total;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public String getPage ()
    {
        return page;
    }

    public void setPage (String page)
    {
        this.page = page;
    }

    public String getTotal_pages ()
    {
        return total_pages;
    }

    public void setTotal_pages (String total_pages)
    {
        this.total_pages = total_pages;
    }

    public Support getSupport ()
    {
        return support;
    }

    public void setSupport (Support support)
    {
        this.support = support;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [per_page = "+per_page+", total = "+total+", data = "+data+", page = "+page+", total_pages = "+total_pages+", support = "+support+"]";
    }
    public class Support
    {
        private String text;

        private String url;

        public String getText ()
        {
            return text;
        }

        public void setText (String text)
        {
            this.text = text;
        }

        public String getUrl ()
        {
            return url;
        }

        public void setUrl (String url)
        {
            this.url = url;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [text = "+text+", url = "+url+"]";
        }
    }

    public class Data
    {
        private String last_name;

        private String id;

        private String avatar;

        private String first_name;

        private String email;

        public String getLast_name ()
        {
            return last_name;
        }

        public void setLast_name (String last_name)
        {
            this.last_name = last_name;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getAvatar ()
        {
            return avatar;
        }

        public void setAvatar (String avatar)
        {
            this.avatar = avatar;
        }

        public String getFirst_name ()
        {
            return first_name;
        }

        public void setFirst_name (String first_name)
        {
            this.first_name = first_name;
        }

        public String getEmail ()
        {
            return email;
        }

        public void setEmail (String email)
        {
            this.email = email;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [last_name = "+last_name+", id = "+id+", avatar = "+avatar+", first_name = "+first_name+", email = "+email+"]";
        }
    }


}
