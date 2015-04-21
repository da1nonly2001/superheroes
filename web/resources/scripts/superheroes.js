/**
 * Created by catop on 4/16/15.
 */
var superheroes =
(function(jQuery){
    var results = {};
    var $ = jQuery;

    init();

    $(window).load(function(){
        $(".js-hide").each(function(){
           $(this).show();
        });
    });

    function init() {
        initBoxHeight();
        initTables();
        initCreatorHandlers();
    }

    function initBoxHeight() {
        $(".js-height").each(function(){
            var maxHeight = 0;
            $(this).find(".border").each(function(){
                var height = $(this).height();
                if (height>maxHeight) {
                    maxHeight = height;
                }
            });
            $(this).find(".border").each(function(){
                $(this).height(maxHeight);
            });
        });
    }

    function initTables() {
        $("table").each(function(){
            var id = $(this).attr('id');
            var pager = "#"+id+"-pager";
            if ($(pager).length > 0)
                $(this).tablesorter({widthFixed: false, widgets:['zebra']}).tablesorterPager({container: pager, size:5});
            else
                $(this).tablesorter({widthFixed: false, widgets:['zebra']});
        });
    }

    function initCreatorHandlers() {
        $(".js-creators-more").click(function(){
            var $parent = $(this).parent();
            var $list = $parent.parent();
            $list.children().show(200);
            $parent.remove();
        });
    }

    return results;
}(jQuery));