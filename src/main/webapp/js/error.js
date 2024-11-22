var t, downSecond = 12;
$("#downTime").html(downSecond + "秒");
t = setInterval(function() {
    downSecond--;
    $("#downTime").html(downSecond + "秒");
    if (downSecond == 0) {
        clearInterval(t);
        turnUrl();
    }
}, 1000);

function turnUrl() {
    if (history.length > 1) {
        history.back();
    } else {
        window.location = "login.jsp";
    }
}

$(function() {
    $("#digit").animate({ count: codeValue }, {
        duration: 600,
        easing: "linear",
        step: function() {
            $("#digit").text(splitK(String(parseInt(this.count))));
        },
        complete: function() {
            $("#digit").text(splitK(codeValue));
        }
    })
})

function splitK(num) {
    if (num == 0) {
        return 0;
    }
    var decimal = String(num).split('.')[1] || '';
    var tempArr = [];
    var revNumArr = String(num).split('.')[0].split("").reverse();
    for (i in revNumArr) {
        tempArr.push(revNumArr[i]);
        if ((i + 1) % 3 === 0 && i != revNumArr.length - 1) {
            tempArr.push(',');
        }
    }
    var zs = tempArr.reverse().join('');
    return decimal ? zs + '.' + decimal : zs;
}