package org.dclm.live.ui.util

import org.dclm.live.ui.message.Blog

interface BlogRecieved {
 fun blogCame(blog: MutableList<Blog>)
}