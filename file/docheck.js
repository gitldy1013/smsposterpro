/**
 * 企企大厅抢单脚本
 * 使用方法：
 *     1.登录进入搜索页面
 *     2.点击页面内需要的搜索偏好条件（不要手动点击排序）
 *     3.修改acticon的值,为3时比较每十万扣息范围,范围区间为[min,max],为4时比较年息,修改年息预期的price数值
 *     4.复制脚本到浏览器console,键入enter键开始执行脚本（可通过F12或设置菜单开发者模式唤出控制台）
 *     5.可刷新页面停止执行脚本
 */
//启动抢单
let action = 4;
let down = document.getElementsByClassName("anticon-caret-down")[action];
down.click();
const price = 37;
const max = 90000;
const min = 100000000;
let flag = false;
let s1 = setInterval(function () {
        let button = document.getElementsByClassName("refresh-button")[0];
        const cls = document.getElementsByClassName("ant-modal-close")[0];
        if (cls !== undefined || !flag) {
            console.log('循环抢单');
            button.click();
        }
    },
    4000);

let s2 = setInterval(function () {
        let operator = document.getElementsByClassName("operate-button")[0];
        const blur = document.getElementsByClassName("ant-spin-blur")[0];
        //实际应用将cls变量替换即可 const cls = document.getElementsByClassName("receive-btn")[0];
        const cls = document.getElementsByClassName("ant-modal-close")[0];
        const textContent = document.getElementsByClassName("ant-table-column-sort")[0];
        const value = textContent.getElementsByTagName("div")[0].innerText.replace("%", "");
        if(action === 4){
            if (blur === undefined && cls === undefined && value >= price) {
                operator.click();
            }
            if (cls !== undefined) {
                cls.click();
                console.log('已抢单');
            }
        }else {
            if (blur === undefined && cls === undefined && (value >= min && value <= max)) {
                operator.click();
            }
            if (cls !== undefined) {
                cls.click();
                console.log('已抢单');
            }
        }
    },
    1000);

//停止抢单
//snippet:///docheck
let s = s1;
if (s2 > s1) {
    s = s2;
    for (var i = 1; i <= s; i++) {
        clearInterval(i);
    }
}

