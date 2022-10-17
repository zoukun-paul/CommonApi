package com.frame.kzou.business.controller.short_link;

import com.frame.kzou.business.pojo.ShortLink;
import com.frame.kzou.business.service.short_link.ShortLinkService;
import com.frame.kzou.common.ResultResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/17
 * Time: 14:25
 * Description: 短链接 （Hash + 布隆过滤器）
 *      1. 获取短链接
 *      2. 统计分析短链接的 点击率
 *      3. 删除短链接（定时/立刻删除） - 短链接的有效时间
 *
 *      =>
 *          重定向  rd开头
 */
@Controller
@RequestMapping
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    public ShortLinkController(ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    @ResponseBody
    @PostMapping("shortLink/encode")
    public ResultResponse encode(String longLink) {
        String slink = shortLinkService.genShortLink(longLink);
        ShortLink shortLink = new ShortLink(longLink, slink, shortLinkService.genSuffix(longLink));
        shortLinkService.saveEntry(shortLink);
        return ResultResponse.success(shortLink);
    }

    @GetMapping("/rd/{surlSuffix}")
    public String redirect(@PathVariable String surlSuffix) {
        ShortLink shortLink = shortLinkService.getByShortUrlSuffix(surlSuffix);
        return "redirect:" + shortLink.getLurl();
    }

}
