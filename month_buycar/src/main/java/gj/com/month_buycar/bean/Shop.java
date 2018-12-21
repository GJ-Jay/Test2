package gj.com.month_buycar.bean;

import java.util.List;

import gj.com.month_buycar.R;

public class Shop {

        /**
         * list : []
         * sellerName :
         * sellerid : 0
         */
        //商品的bean 多写了三个东西 商品字体颜色 背景 选中状态
        private List<Goods> list;
        private String sellerName;
        private String sellerid;
        int textColor = 0xffffffff;
        int background = R.color.grayblack;

        boolean check;

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }

        public String getSellerid() {
            return sellerid;
        }

        public void setSellerid(String sellerid) {
            this.sellerid = sellerid;
        }

        public List<Goods> getList() {
            return list;
        }

        public void setList(List<Goods> list) {
            this.list = list;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public int getBackground() {
            return background;
        }

        public void setBackground(int background) {
            this.background = background;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }
}
