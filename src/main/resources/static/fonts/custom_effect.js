(function ($) {
    "use strict";
    var isOn = 0, sets, fx, toAnimate = "#font-effect", settings = {
        animation: 8,
        animationType: "in",
        backwards: false,
        easing: "easeOutQuint",
        speed: 1000,
        sequenceDelay: 100,
        startDelay: 0,
        offsetX: 100,
        offsetY: 50,
        onComplete: doThis,
        restoreHTML: true
    };
    jQuery(document).ready(function () {
        fx = jQuery("#font-effect");
        jQuery.cjTextFx(settings);
        jQuery.cjTextFx.animate(toAnimate);
    });

    function doThis() {
        fx = jQuery("#font-effect-info");
        var animateObj = '#font-effect-info';
        if (isOn === 13) {
            fx.html('我会为您持续更新~尽情期待！');
            sets = {animation: 1, animationType: "in", restoreHTML: false, onComplete: false};
        } else {
            (isOn < 13) ? isOn++ : isOn = 0;
            switch (isOn) {
                case 1:
                    fx.html("这是一款可以爬取本地文件的服务");
                    sets = {animation: 2, animationType: "in", easing: "easeInQuint", restoreHTML: false,};
                    break;
                case 2:
                    sets = {animation: 11, animationType: "out", restoreHTML: false};
                    break;
                case 3:
                    fx.html("当然，它也不限制于爬取本地文件");
                    sets = {animation: 11, animationType: "in", restoreHTML: false};
                    break;
                case 4:
                    sets = {animation: 5, animationType: "out", restoreHTML: false};
                    break;
                case 5:
                    fx.html("支持文件导入，索引，导出，下载");
                    sets = {animation: 1};
                    break;
                case 6:
                    sets = {animation: 1, animationType: "out", restoreHTML: false};
                    break;
                case 7:
                    fx.html("支持自定义检索条件检索你的数据");
                    sets = {animation: 6, backwards: true};
                    break;
                case 8:
                    sets = {animation: 4, animationType: "out", backwards: true, restoreHTML: false};
                    break;
                case 9:
                    fx.html("有它，你不再需要漫长的复制黏贴");
                    sets = {animation: 2, easing: "easeOutBounce"};
                    break;
                case 10:
                    sets = {animation: 2, animationType: "out", speed: 500, easing: "easeInBack", restoreHTML: false};
                    break;
                case 11:
                    fx.html("页面简洁干净，后台文件定时清理");
                    sets = {animation: 14, startDelay: 1000, easing: "easeInBack", restoreHTML: false};
                    break;
                case 12:
                    sets = {animation: 6, animationType: "out", speed: 500, easing: "easeInBack", restoreHTML: false};
                    break;
                default:
                    isOn = 13;
                    break;
            }
        }
        jQuery.cjTextFx.animate(animateObj, sets);
    }
})(jQuery);
