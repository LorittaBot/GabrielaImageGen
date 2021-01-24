
<p align="center">
<h1 align="center">💞 Lovely Design 💞</h1>

<p align="center">
<a href="https://mrpowergamerbr.com/"><img src="https://img.shields.io/badge/website-mrpowergamerbr-fe4221.svg"></a>
<a href="https://github.com/LorittaBot/Loritta/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-AGPL%20v3-lightgray.svg"></a>
</p>

> "What a lovely design!"

Lovely Design is a collection of tiny (S)CSS classes that can be reused in different projects.

Yes, there already is a lot of other "CSS Frameworks" like [Bootstrap](https://getbootstrap.com/), [Bulma](https://bulma.io/), [Tailwind](https://tailwindcss.com/)... but we decided to create our own smol framework because...
* We wanted something small that can be easily integrated into already existing projects, so nothing that when you include it in your project it already styles a lot of stuff that you *don't* want to be styled.
* We didn't want a complete JS + CSS framework or something that requires a lot of JS to work well.
* We didn't want a framework that *forces* you to do your own stuff their way. "It is my code so I want to code it my own way!"
* We like to reinvent the wheel. 🤪

## 🤔 How to use?

Clone the repository

Create a style.scss file

Some packages requires a `primary-color` variable
`$primary-color: #00c0ff;`

Lovely Style works in a "programming" like style, you need to *import* the file for it to be added to your spreadsheet. If a style requires argument, you need to call *include* them.

### Styles

#### reset.scss
```scss
@import 'lovely-style/rainbow.scss'
```

Because some browsers are dumb and provide their own styles, causing inconsistencies between browsers

#### body.scss
```scss
@import 'lovely-style/body.scss'

body {
	@include lovely-body($background-color: #f3f3f3, $text-color: #333333, $hide-overflow-x: false)
}
```

Creates a pretty "default" body, all variables are optional.

There is also a `hide-overflow-x`, useful if you have those pesky bugs that cause a horizontal scroll bar to show up.

#### buttons.scss
```scss
@import 'lovely-style/buttons.scss'

lovely-button-colors() // Imports default button colors

// You can also create your own colors
button-color("color-name", #fafafa)
```

Buttons!

```html
<div class="button primary">
This is a button
</div>

<div class="button red">
Angry button grrr
</div>

text text text <span class="button pink is-inline">owo</span> text text text
```

#### emotes.scss
```scss
@import 'lovely-style/emotes.scss'
```
Sometimes you want to have *custom* emotes (with images) in your texts, `emotes.scss` allows you to place inline image emotes, and it looks cool!

```html
I love you! <img src="/assets/img/lori_heart.png" class="inline-emoji" />
```

#### fancy-hr.scss
```scss
@import 'lovely-style/fancy-hr.scss'
```
A fancy `<hr>` tag, because browser's default is ugly.

#### fancy-links.scss
```scss
@import 'lovely-style/fancy-links.scss'
```
A fancy `<a>` tag, because browser's default is ugly.


#### fancy-table.scss
```scss
@import 'lovely-style/fancy-table.scss'
```
Fancy tables with horizontal divisors between cells and a nice table header with your website's primary color!

```html
<table class="fancy-table">
	<tr>
		<th>Name</th>
		<th>Date</th>
	</tr>
	<tr>
		<td>Lovely Style</td>
		<td>2020</td>
	</tr>
	<tr>
		<td>Loritta</td>
		<td>2017</td>
	</tr>
	<tr>
		<td>SparklyPower</td>
		<td>2014</td>
	</tr>
</table>
```

#### navigation-bar.scss
```scss
@import 'lovely-style/navigation-bar.scss'

@include  navigation-bar($navbar-color: $primary-color, $navbar-font-family: "Oswald, Impact, Arial, sans-serif", $navbar-height: 46px);
```

A nice navigation bar for your website!

```html
<nav class="navigation-bar">
	<div class="left-side-entries">
		<div class="entry">
			My Website
		</div>
		<div class="entry">
			<a href="https://loritta.website/">
				The Best Website Ever!
			</a>
		</div>
	</div>
</div>
```

There is also a fixed navbar version.
```html
<nav class="navigation-bar fixed">
	<div class="left-side-entries">
		<div class="entry">
			My Website
		</div>
		<div class="entry">
			<a href="https://loritta.website/">
				The Best Website Ever!
			</a>
		</div>
	</div>
</div>
<div class="dummy-navigation-bar"></div>
```

(The `dummy-navigation-bar` creates a empty div with the same height as your navbar, because a `fixed` navbar does not occupy space in the page, so you need to use it to avoid text staying behind your navbar)

#### overflow.scss
```scss
@import 'lovely-style/overflow.scss'
```

Adds some nice to have overflow classes

* `hide-overflow`: hides all overflow
* `hide-horizontal-overflow`: hides all horizontal overflow
* `hide-vertical-overflow`: hides all vertical overflow

#### primary-color-headings.scss
```scss
@import 'lovely-style/primary-color-headings.scss'
```

Changes your `h1`...`h6` tags text to have the same color as your website's primary color. Fancy!


#### rainbow.scss
```scss
@import 'lovely-style/rainbow.scss'
```

Adds a rainbow effect to the selected text

```html
<p class="has-rainbow-text">
Asriel Dreemurr: God of Hyperdeath
</p>
```

#### wrap-pre.scss
```scss
@import 'lovely-style/wrap-pre.scss'
```

Adds overflow to `pre` elements