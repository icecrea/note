var SUCCESS = 0;
var ERROR = 1;

$(function(){
    var userId = getCookie('userId');
    console.log(userId);
  //网页加载以后, 立即读取笔记本列表
    loadNotebooks();
    $('#notebook-list').on('click','.notebook',loadNotes);
    $('#note-list').on('click','.note',loadNote);
    $('#note-list').on('click', '#add_note', showAddNoteDialog);
    $('#can').on('click','.close,.cancel',closeDialog);
    $('#can').on('click','.create-note',addNote);
  //绑定点击保存笔记事件
    $('#save_note').on('click', updateNote);

	//绑定笔记子菜单的触发事件
	$('#note-list').on('click', '.btn-note-menu', showNoteMenu);
	//监听整体的文档区域, 任何位置点击都要关闭笔记子菜单
	$(document).click(hideNoteMenu);
	
	//监听笔记子菜单中移动按钮的点击
	$('#note-list').on('click', '.btn_move', showMoveNoteDialog);
	//监听移动笔记对话框中的确定按钮
	$('#can').on('click', '.move-note', moveNote);
});

/** 加载笔记本列表数据 */
function loadNotebooks(){
    //利用ajax从服务器获取(get)数据, 使用getJSON方法
    var url = 'notebook/list.do';
    var data = {userId:getCookie('userId'),
            name:'demo'};
    $.getJSON(url, data, function(result){
        console.log(result);
        if(result.state==SUCCESS){
            var notebooks = result.data;
            //在showNotebooks方法中将全部的
            //笔记本数据 notebooks 显示到 
            // notebook-list 区域
            showNotebooks(notebooks);
        }else{
            alert(result.message);
        }
    });
}


/** 在notebook-list区域中显示笔记本列表 */
function showNotebooks(notebooks){
    //算法: 
    //找显示笔记本列表的区域的ul元素
    //遍历notebooks数组, 将为每个对象创建一个li
    //元素, 添加到 ul元素中.
    var ul = $('#notebook-list ul');
    ul.empty();//清除ul中原有的内容
    for(var i=0; i<notebooks.length; i++){
        var notebook = notebooks[i];
        var li = notebookTemplate.replace(
                '[name]', notebook.name);
        li = $(li);
        li.data('notebookId', notebook.id);
        ul.append(li);
    }
}

/** 将笔记列表信息显示到屏幕上 */
function showNotes(notes){
    console.log(notes); 
    //将每个笔记对象显示到屏幕的ul区域
    var ul = $('#note-list ul');
    ul.empty();
    for(var i=0; i<notes.length; i++){
        var note = notes[i];
        var li = noteTemplate.replace(
                '[title]', note.title);
        li = $(li);
        li.data("noteId",note.id);
        ul.append(li);
    }
}

/** 笔记本项目点击事件处理方法, 加载全部笔记 */
function loadNotes(){
    var li = $(this);//当前被点击的对象li

    //在被点击的笔记本li增加选定效果
    li.parent().find('a').removeClass('checked');
    li.find('a').addClass('checked');

    var url = 'note/list.do';

    //li.data('notebookId') 方法可以获取绑定到li
    //元素上的数据notebookId, 这个notebookId在
    //showNotebooks方法中绑定li元素上的!

    var data={notebookId:li.data('notebookId')};
    console.log(data);
    $(document).data('notebookId', li.data('notebookId'));
    $.getJSON(url, data, function(result){
        if(result.state==SUCCESS){
            var notes = result.data;
            showNotes(notes);
        }else{
            alert(result.message);
        }
    });
}

function loadNote(){
    //获取当前点击的 li 元素
    var li = $(this);
    //获取在显示时候绑定到li中的笔记ID值
    var id = li.data('noteId');

    //设置选中高亮效果
    li.parent().find('a').removeClass('checked');
    li.find('a').addClass('checked');

    var url = 'note/load.do';
    var data= {noteId: id };

    $.getJSON(url, data, function(result){
        //console.log(result);
        if(result.state==SUCCESS){
            var note = result.data;
            showNote(note);
        }else{
            alert(result.message);
        }
    });
}

function showNote(note){
    //显示笔记标题
    $('#input_note_title').val(note.title);
    //显示笔记内容
    um.setContent(note.body);
    
    //绑定笔记信息, 用于保存操作
    $(document).data('note', note);
}

function closeDialog(){
    $('.opacity_bg').hide();
    $('#can').empty();
}

function showAddNoteDialog(){
    var id = $(document).data('notebookId');
    if(id){
        $('#can').load('alert/alert_note.html', function(){
            $('#input_note').focus();
        });
        $('.opacity_bg').show();
        return;
    }
    alert('必须选择笔记本!');
}

function addNote(){
    var url = 'note/add.do';
    var notebookId=$(document).data('notebookId');
    var title = $('#can #input_note').val();

    var data = {userId:getCookie('userId'),
        notebookId:notebookId,
        title:title};
    //console.log(data);

    $.post(url, data, function(result){
        if(result.state==SUCCESS){
            var note=result.data;
            //console.log(note);
            showNote(note);
            //找到显示笔记列表的ul对象
            var ul = $('#note-list ul');
            //创建新新的笔记列表项目 li 
            var li = noteTemplate.replace(
                    '[title]', note.title);
            li = $(li);
            //设置选定效果
            ul.find('a').removeClass('checked');
            li.find('a').addClass('checked');
            //插入到笔记列表的第一个位置
            ul.prepend(li);
            //关闭添加对话框
            closeDialog();   
        }else{
            alert(result.message);
        }
    });
}


function updateNote(){
    var url = 'note/update.do';
    var note = $(document).data('note');
    var data = {noteId:note.id};
    var modified = false;
    var title = $('#input_note_title').val();
    if(title && title!=note.title){
        data.title = title;
        modified = true;
    }
    var body = um.getContent();
    if(body && body != note.body ){
        data.body = body;
        modified = true;
    }
    if(modified){

        $.post(url, data, function(result){

            if(result.state == 0){
                //console.log("Success!");
                //内存中的 note 改成新的数据
                note.title = title;
                note.body = body;
                var l = $('#note-list .checked').parent();
                $('#note-list .checked').remove()
                var li = noteTemplate.replace( '[title]', title);
                var a = $(li).find('a');
                a.addClass('checked');
                l.prepend(a);
            }else{
                alert(result.mesage);
            }
        });
    }
}

function showNoteMenu(){
	//找到菜单对象, 调用show() 方法
    var btn = $(this);
    //如果当前是被选定的 笔记项目 就弹出子菜单
	btn.parent('.checked').next().toggle();
	//btn.parent('.checked') 获取当前按钮的父元素
    //这个元素必须符合选择器'.checked', 如果不
    //符合就返回空的JQuery元素.  
    return false;//阻止点击事件的继续传播!避免传播到document对象时候, 触发关闭菜单事件
}

/** 关闭笔记子菜单事件处理方法 */
function hideNoteMenu(){
    $('.note_menu').hide();
}

/** 显示移动笔记对话框 */
function showMoveNoteDialog(){
    var id = $(document).data('note').id;
    if(id){
        $('#can').load('alert/alert_move.html', loadNotebookOptions);
        $('.opacity_bg').show();
        return;
    }
    alert('必须选择笔记!');
} 

/** 加载移动笔记对话框中的笔记本列表 */
function loadNotebookOptions(){
    var url = 'notebook/list.do';
    var data={userId:getCookie('userId')};
    $.getJSON(url, data, function(result){
        if(result.state==SUCCESS){
            var notebooks = result.data;
            //清楚全部的笔记本下拉列表选项
            //添加新的笔记本列表选项
            $('#moveSelect').empty();
            var id=$(document).data('notebookId');
            for(var i=0; i<notebooks.length; i++){
                var notebook = notebooks[i];
                var opt=$('<option></option>')
                    .val(notebook.id)
                    .html(notebook.name);
                //默认选定当时笔记的笔记本ID
                if(notebook.id==id){
                    opt.attr('selected','selected');
                }
                $('#moveSelect').append(opt);
            }
        }else{
            alert(result.message);
        }
    });
}

/** 移动笔记事件处理方法 */
function moveNote(){

    var url = 'note/move.do';
    var id = $(document).data('note').id;
    var bookId=$('#moveSelect').val();
    //笔记本ID没有变化, 就不移动了!
    if(bookId==$(document).data('notebookId')){
        return;
    }
    var data = {noteId:id, notebookId:bookId};
    $.post(url, data, function(result){
        if(result.state==SUCCESS){
            //移动成功, 在当前笔记列表中删除移动的笔记
            //将笔记列表中的第一个设置为当前笔记, 否则清空边编辑区域
            var li = $('#note-list .checked').parent();
            var lis = li.siblings();
            if(lis.size()>0){
                lis.eq(0).click();
            }else{
                $('#input_note_title').val("");
                um.setContent("");
            }
            li.remove();
            closeDialog();//关闭对话框!
        }else{
            alert(result.message);
        }
    });
}

//重构笔记列表模板, 为笔记子菜单触发按钮添加类 btn-note-menu
var noteTemplate = '<li class="online note">'+
'<a>'+
'<i class="fa fa-file-text-o" title="online" rel="tooltip-bottom"></i> [title]<button type="button" class="btn btn-default btn-xs btn_position btn_slide_down btn-note-menu"><i class="fa fa-chevron-down"></i></button>'+
'</a>'+
'<div class="note_menu" tabindex="-1">'+
'<dl>'+
    '<dt><button type="button" class="btn btn-default btn-xs btn_move" title="移动至..."><i class="fa fa-random"></i></button></dt>'+
    '<dt><button type="button" class="btn btn-default btn-xs btn_share" title="分享"><i class="fa fa-sitemap"></i></button></dt>'+
    '<dt><button type="button" class="btn btn-default btn-xs btn_delete" title="删除"><i class="fa fa-times"></i></button></dt>'+
'</dl>'+
'</div>'+
'</li>';

var notebookTemplate = 
    '<li class="online notebook">'+
    '<a><i class="fa fa-book" title="online" '+
    'rel="tooltip-bottom"></i> [name]</a>'+
    '</li>';
