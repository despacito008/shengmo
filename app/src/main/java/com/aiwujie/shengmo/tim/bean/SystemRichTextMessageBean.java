package com.aiwujie.shengmo.tim.bean;

public class SystemRichTextMessageBean {

    /**
     * costomMassageType : customMessageTypeSystemRichText
     * contentDict : {"content":"您关注的主播 鸿雁传书12 现在开播了，快去看看吧~","highlightWhenMatchKeywords":{"keyid":"250385","keyword":"鸿雁传书12","keytype":0,"extend":""}}
     */

    private String costomMassageType;
    private ContentDictBean contentDict;

    public String getCostomMassageType() {
        return costomMassageType;
    }

    public void setCostomMassageType(String costomMassageType) {
        this.costomMassageType = costomMassageType;
    }

    public ContentDictBean getContentDict() {
        return contentDict;
    }

    public void setContentDict(ContentDictBean contentDict) {
        this.contentDict = contentDict;
    }

    public static class ContentDictBean {
        /**
         * content : 您关注的主播 鸿雁传书12 现在开播了，快去看看吧~
         * highlightWhenMatchKeywords : {"keyid":"250385","keyword":"鸿雁传书12","keytype":0,"extend":""}
         */

        private String content;
        private HighlightWhenMatchKeywordsBean highlightWhenMatchKeywords;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public HighlightWhenMatchKeywordsBean getHighlightWhenMatchKeywords() {
            return highlightWhenMatchKeywords;
        }

        public void setHighlightWhenMatchKeywords(HighlightWhenMatchKeywordsBean highlightWhenMatchKeywords) {
            this.highlightWhenMatchKeywords = highlightWhenMatchKeywords;
        }

        public static class HighlightWhenMatchKeywordsBean {
            /**
             * keyid : 250385
             * keyword : 鸿雁传书12
             * keytype : 0
             * extend :
             */

            private String keyid;
            private String keyword;
            private String keytype;
            private String extend;
            private String keyword_color;

            public String getKeyword_color() {
                return keyword_color;
            }

            public void setKeyword_color(String keyword_color) {
                this.keyword_color = keyword_color;
            }

            public String getKeyid() {
                return keyid;
            }

            public void setKeyid(String keyid) {
                this.keyid = keyid;
            }

            public String getKeyword() {
                return keyword;
            }

            public void setKeyword(String keyword) {
                this.keyword = keyword;
            }

            public String getKeytype() {
                return keytype;
            }

            public void setKeytype(String keytype) {
                this.keytype = keytype;
            }

            public String getExtend() {
                return extend;
            }

            public void setExtend(String extend) {
                this.extend = extend;
            }
        }
    }
}
