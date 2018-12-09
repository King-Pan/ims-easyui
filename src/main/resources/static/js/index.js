$(function () {
    $("#menuTree").tree(
        {
            url: 'menu/treeMenu',
            method: 'post',
            animate: true,
            onClick: function (node) {
                if ($(this).tree('isLeaf', node.target)
                    && node.attributes.url != undefined) {
                    addTab(node.text, node.attributes.url);
                } else {
                    $(this).tree(
                        node.state === 'closed' ? 'expand'
                            : 'collapse', node.target);
                    node.state = node.state === 'closed' ? 'open'
                        : 'closed';
                }
            }
        });
});

function addTab(title, url) {
    //如果存在tab，激活该tab为选中状态,return.
    if ($("#mainTab").tabs('exists', title)) {
        $("#mainTab").tabs('select', title);
        return;
    }
    //不存在，创建一个新德tab
    $("#mainTab").tabs(
        "add",
        {
            title: title,
            closable: true,
            content: '<iframe scrolling="auto" frameborder="0"  src="'
                + url
                + '" style="width:100%;height:100%;"></iframe>'
        });
}