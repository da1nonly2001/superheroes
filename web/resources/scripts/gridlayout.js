/**
 * Created by catop on 2/26/15.
 */
var gridLayout =
(function(jQuery){
    var results = {};
    var MAX_WIDTH = 960;

    function execute(){
        adjustMargins();
    }
    results.execute = execute;

    function adjustMargins(){
        var docWidth = jQuery(document).width();

        if (docWidth > MAX_WIDTH) {
            jQuery(".row").each(function(){
                jQuery(this).children().first().css('margin-left','0%');
                jQuery(this).children().last().css('margin-right','0%');
            });
        }
    }

    jQuery(document).ready(function() {
        execute();
    });

    jQuery(window).resize(function() {
        execute();
    });

    return results;
}(jQuery));