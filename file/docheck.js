/**
 * 企企大厅抢单脚本
 * 使用方法：
 *     1.登录进入搜索页面
 *     2.点击页面内需要的搜索偏好条件（不要手动点击排序）
 *     3.修改预期的price数值
 *     4.复制脚本到浏览器console,键入enter键开始执行脚本（可通过F12或设置菜单开发者模式唤出控制台）
 *     5.可刷新页面停止执行脚本
 */

let down = document.getElementsByClassName("anticon-caret-down")[4];
down.click();
const price = 37;
setInterval(function () {
        let button = document.getElementsByClassName("refresh-button")[0];
        const cls = document.getElementsByClassName("ant-modal-close")[0];
        const textContent = document.getElementsByClassName("ant-table-column-sort")[0];
        const value = textContent.getElementsByTagName("div")[0].innerText.replace("%","");
        if (cls !== undefined || value < price) {
            button.click();
        }
    },
    4000);

setInterval(function () {
        let operator = document.getElementsByClassName("operate-button")[0];
        const blur = document.getElementsByClassName("ant-spin-blur")[0];
        const cls = document.getElementsByClassName("ant-modal-close")[0];
        const textContent = document.getElementsByClassName("ant-table-column-sort")[0];
        const value = textContent.getElementsByTagName("div")[0].innerText.replace("%","");
        console.log(value);
        //实际应用将cls变量替换即可 const cls = document.getElementsByClassName("receive-btn")[0];
        if (blur === undefined && cls === undefined && value >= price) {
            operator.click();
        }
        if (cls !== undefined) {
            cls.click();
            console.log('已抢单');
        }
    },
    1000);
